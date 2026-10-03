#!/usr/bin/env bash

# Mini RAG Engine - defensive local startup
#
# This script turns the manual setup checklist into one idempotent command. It
# validates/repairs local prerequisites, verifies both Ollama model APIs, builds
# every Maven module, checks the executable JAR, starts Spring Boot, and refuses
# to report success until the health endpoint answers successfully.
#
# Safety rules:
#   * Never deletes an existing SQLite database or Ollama model.
#   * Never kills an unknown process merely because it owns a required port.
#   * Installs missing tools automatically only on macOS when Homebrew already
#     exists. It never installs Homebrew by executing a remote shell script.
#   * Re-pulls a model only if it is missing or its real API smoke test fails.

set -Eeuo pipefail
IFS=$'\n\t'

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
readonly SCRIPT_DIR
readonly RUN_DIR="${SCRIPT_DIR}/.run"
readonly LOCK_DIR="${RUN_DIR}/startup.lock"
readonly APP_LOG="${RUN_DIR}/application.log"
readonly OLLAMA_LOG="${RUN_DIR}/ollama.log"
readonly APP_PID_FILE="${RUN_DIR}/application.pid"
readonly OLLAMA_PID_FILE="${RUN_DIR}/ollama.pid"
readonly JAR_PATH="${SCRIPT_DIR}/rag-application/target/rag-application-0.1.0-SNAPSHOT.jar"

# Environment variables remain the normal plug-and-play mechanism. Defaults
# exactly match application.yml, except 127.0.0.1 is used to avoid an unusual
# hosts-file mapping for localhost.
export RAG_CHAT_MODEL="${RAG_CHAT_MODEL:-llama2}"
export RAG_EMBEDDING_MODEL="${RAG_EMBEDDING_MODEL:-embeddinggemma}"
export RAG_CHAT_BASE_URL="${RAG_CHAT_BASE_URL:-http://127.0.0.1:11434/v1}"
export RAG_EMBEDDING_BASE_URL="${RAG_EMBEDDING_BASE_URL:-http://127.0.0.1:11434/v1}"
export SERVER_PORT="${SERVER_PORT:-8080}"

readonly OLLAMA_ORIGIN="${OLLAMA_ORIGIN:-http://127.0.0.1:11434}"
readonly STARTUP_TIMEOUT_SECONDS="${STARTUP_TIMEOUT_SECONDS:-90}"
readonly MODEL_TIMEOUT_SECONDS="${MODEL_TIMEOUT_SECONDS:-240}"

MODE="foreground"
AUTO_INSTALL=true
STARTED_APP=false
APP_PID=""
TAIL_PID=""
TEMP_FILES=""

usage() {
    cat <<'EOF'
Usage: ./start-local.sh [option]

Default behavior:
  Repair/check prerequisites, verify local Ollama models, run a clean Maven
  package, start Spring Boot, verify /actuator/health, and remain attached until
  Ctrl+C is pressed.

Options:
  --background   Start Spring Boot, verify health, then return to the shell.
  --check-only   Diagnose/repair prerequisites, models, and build; do not start Spring.
  --no-install   Diagnose missing/outdated tools but do not install them.
  --help         Show this help text.

Common overrides:
  RAG_CHAT_MODEL=another-chat-model ./start-local.sh
  RAG_EMBEDDING_MODEL=another-embedding-model ./start-local.sh
  SERVER_PORT=8081 ./start-local.sh

Runtime artifacts and logs are stored under .run/ and are ignored by Git.
EOF
}

log() {
    printf '[startup] %s\n' "$*"
}

warn() {
    printf '[startup] WARNING: %s\n' "$*" >&2
}

die() {
    printf '[startup] ERROR: %s\n' "$*" >&2
    exit 1
}

# Print the command/line that failed. This turns set -e from a mysterious early
# exit into an actionable diagnostic.
on_error() {
    local exit_code=$?
    local line_number=$1
    printf '[startup] ERROR: command failed at line %s with exit code %s\n' \
        "$line_number" "$exit_code" >&2
    printf '[startup] Application log: %s\n' "$APP_LOG" >&2
    printf '[startup] Ollama log: %s\n' "$OLLAMA_LOG" >&2
    exit "$exit_code"
}
trap 'on_error "$LINENO"' ERR

# Stop only a Spring process started by this invocation. Ollama deliberately
# remains running because it is a shared local service and may have existed
# before this script.
cleanup() {
    local exit_code=$?
    trap - ERR INT TERM EXIT

    if [[ -n "$TAIL_PID" ]] && kill -0 "$TAIL_PID" 2>/dev/null; then
        kill "$TAIL_PID" 2>/dev/null || true
    fi

    if [[ "$STARTED_APP" == true && "$MODE" == "foreground" && -n "$APP_PID" ]] \
       && kill -0 "$APP_PID" 2>/dev/null; then
        log "Stopping Spring Boot process ${APP_PID}"
        kill -TERM "$APP_PID" 2>/dev/null || true
        wait "$APP_PID" 2>/dev/null || true
    fi

    if [[ -n "$TEMP_FILES" ]]; then
        # TEMP_FILES contains paths created exclusively by mktemp in this run.
        # Word splitting is intentional because Bash 3.2 has no convenient
        # portable mapfile for an array populated across helper calls.
        local old_ifs=$IFS
        IFS=' '
        # shellcheck disable=SC2086
        rm -f $TEMP_FILES
        IFS=$old_ifs
    fi

    rm -f "${LOCK_DIR}/pid" 2>/dev/null || true
    rmdir "$LOCK_DIR" 2>/dev/null || true
    exit "$exit_code"
}
trap cleanup EXIT INT TERM

for argument in "$@"; do
    case "$argument" in
        --background) MODE="background" ;;
        --check-only) MODE="check-only" ;;
        --no-install) AUTO_INSTALL=false ;;
        --help|-h) usage; exit 0 ;;
        *) die "Unknown option: ${argument}. Use --help." ;;
    esac
done

mkdir -p "$RUN_DIR"

# Atomic directory creation prevents two startup scripts from racing over Maven
# clean output, PID files, or ports. A dead owner's lock is recovered safely.
if ! mkdir "$LOCK_DIR" 2>/dev/null; then
    lock_pid="$(cat "${LOCK_DIR}/pid" 2>/dev/null || true)"
    if [[ "$lock_pid" =~ ^[0-9]+$ ]] && kill -0 "$lock_pid" 2>/dev/null; then
        die "Another startup script is running with PID ${lock_pid}"
    fi
    warn "Recovering a stale startup lock"
    rm -f "${LOCK_DIR}/pid" 2>/dev/null || true
    rmdir "$LOCK_DIR" 2>/dev/null || die "Cannot recover ${LOCK_DIR}; inspect it manually"
    mkdir "$LOCK_DIR"
fi
printf '%s\n' "$$" > "${LOCK_DIR}/pid"

command_exists() {
    command -v "$1" >/dev/null 2>&1
}

validate_local_model_configuration() {
    local expected_base="${OLLAMA_ORIGIN%/}/v1"

    # Model identifiers are inserted into small diagnostic JSON requests. The
    # accepted Ollama-name character set prevents malformed JSON and accidental
    # shell-like content without restricting ordinary namespace/tag syntax.
    if ! [[ "$RAG_CHAT_MODEL" =~ ^[A-Za-z0-9._:/-]+$ ]]; then
        die "RAG_CHAT_MODEL contains unsupported characters: ${RAG_CHAT_MODEL}"
    fi
    if ! [[ "$RAG_EMBEDDING_MODEL" =~ ^[A-Za-z0-9._:/-]+$ ]]; then
        die "RAG_EMBEDDING_MODEL contains unsupported characters: ${RAG_EMBEDDING_MODEL}"
    fi

    # This script intentionally manages a local Ollama process. Remote/provider
    # URLs remain supported by the Java application, but should not trigger
    # local package installation or model pulls under a misleading command.
    case "$RAG_CHAT_BASE_URL" in
        "$expected_base"|http://127.0.0.1:11434/v1|http://localhost:11434/v1) ;;
        *) die "start-local.sh manages local Ollama only; RAG_CHAT_BASE_URL is ${RAG_CHAT_BASE_URL}" ;;
    esac
    case "$RAG_EMBEDDING_BASE_URL" in
        "$expected_base"|http://127.0.0.1:11434/v1|http://localhost:11434/v1) ;;
        *) die "start-local.sh manages local Ollama only; RAG_EMBEDDING_BASE_URL is ${RAG_EMBEDDING_BASE_URL}" ;;
    esac
}

check_free_disk_space() {
    local available_kb
    available_kb="$(df -Pk "$SCRIPT_DIR" | awk 'NR==2 {print $4}')"
    if [[ "$available_kb" =~ ^[0-9]+$ ]] && (( available_kb < 5242880 )); then
        warn "Less than 5 GiB is free. Maven/model downloads may fail; available KiB: ${available_kb}"
    fi
}

require_homebrew_for_repair() {
    if [[ "$(uname -s)" != "Darwin" ]]; then
        die "$1 is missing/outdated. Automatic package installation is supported only on macOS; install it and rerun."
    fi
    command_exists brew || die "$1 is missing/outdated and Homebrew is unavailable. Install Homebrew or install $1 manually."
    [[ "$AUTO_INSTALL" == true ]] || die "$1 is missing/outdated and --no-install was requested"
}

java_major_version() {
    java -version 2>&1 | awk -F '[".]' '/version/ { print $2; exit }'
}

activate_homebrew_java21() {
    local java_prefix
    java_prefix="$(brew --prefix openjdk@21 2>/dev/null)" || return 1
    export JAVA_HOME="${java_prefix}/libexec/openjdk.jdk/Contents/Home"
    export PATH="${JAVA_HOME}/bin:${PATH}"
}

ensure_java() {
    local major=""
    if command_exists java; then
        major="$(java_major_version || true)"
    fi

    if [[ "$major" =~ ^[0-9]+$ ]] && (( major >= 21 )); then
        log "Java $(java -version 2>&1 | head -n 1)"
        return
    fi

    require_homebrew_for_repair "Java 21"

    # Java 21 may already be installed by Homebrew but absent from PATH because
    # openjdk@21 is a versioned (keg-only) formula. Activate it before doing any
    # unnecessary reinstall/download.
    if brew list --versions openjdk@21 >/dev/null 2>&1 && activate_homebrew_java21; then
        major="$(java_major_version || true)"
        if [[ "$major" =~ ^[0-9]+$ ]] && (( major >= 21 )); then
            log "Activated existing Homebrew Java: $(java -version 2>&1 | awk 'NR==1 {print; exit}')"
            return
        fi
    fi

    log "Java 21 is missing/outdated; installing or repairing openjdk@21 with Homebrew"
    if brew list --versions openjdk@21 >/dev/null 2>&1; then
        brew reinstall openjdk@21
    else
        brew install openjdk@21
    fi
    activate_homebrew_java21 || die "Homebrew installed openjdk@21 but JAVA_HOME could not be resolved"

    major="$(java_major_version || true)"
    if ! [[ "$major" =~ ^[0-9]+$ ]] || (( major < 21 )); then
        die "Java repair completed but Java 21+ is still not active"
    fi
    log "Java repaired: $(java -version 2>&1 | awk 'NR==1 {print; exit}')"
}

maven_is_supported() {
    local version major minor
    version="$(mvn -version 2>/dev/null | awk 'NR==1 { print $3 }')"
    major="${version%%.*}"
    version="${version#*.}"
    minor="${version%%.*}"
    [[ "$major" =~ ^[0-9]+$ && "$minor" =~ ^[0-9]+$ ]] \
        && (( major > 3 || (major == 3 && minor >= 9) ))
}

ensure_maven() {
    if command_exists mvn && maven_is_supported; then
        log "$(mvn -version | head -n 1)"
        return
    fi

    require_homebrew_for_repair "Maven 3.9+"
    log "Maven 3.9+ is missing/outdated; installing or upgrading it with Homebrew"
    if brew list --versions maven >/dev/null 2>&1; then
        brew upgrade maven || brew reinstall maven
    else
        brew install maven
    fi
    if ! command_exists mvn || ! maven_is_supported; then
        die "Maven repair completed but Maven 3.9+ is still unavailable"
    fi
    log "Maven repaired: $(mvn -version | awk 'NR==1 {print; exit}')"
}

ensure_curl() {
    command_exists curl || die "curl is required for service/API health checks"
}

ensure_ollama_cli() {
    if command_exists ollama && ollama --version >/dev/null 2>&1; then
        log "$(ollama --version 2>&1 | head -n 1)"
        return
    fi

    require_homebrew_for_repair "Ollama"
    log "Ollama is missing or its CLI is broken; installing or repairing it with Homebrew"
    if brew list --versions ollama >/dev/null 2>&1; then
        brew reinstall ollama
    else
        brew install ollama
    fi
    if ! command_exists ollama || ! ollama --version >/dev/null 2>&1; then
        die "Ollama repair completed but its CLI still does not work"
    fi
}

url_responds() {
    curl --fail --silent --show-error --max-time 3 "$1" >/dev/null 2>&1
}

show_port_owner() {
    local port=$1
    if command_exists lsof; then
        lsof -nP -iTCP:"$port" -sTCP:LISTEN >&2 || true
    fi
}

wait_for_url() {
    local url=$1
    local timeout=$2
    local label=$3
    local elapsed=0
    while (( elapsed < timeout )); do
        if url_responds "$url"; then
            return 0
        fi
        sleep 1
        elapsed=$((elapsed + 1))
    done
    warn "Timed out after ${timeout}s waiting for ${label}: ${url}"
    return 1
}

ensure_ollama_server() {
    local version_url="${OLLAMA_ORIGIN}/api/version"
    if url_responds "$version_url"; then
        log "Ollama server is ready at ${OLLAMA_ORIGIN}"
        return
    fi

    # Never kill or replace an unknown listener. Doing so could disrupt another
    # developer service and would make a supposedly safe script destructive.
    if command_exists lsof && lsof -nP -iTCP:11434 -sTCP:LISTEN >/dev/null 2>&1; then
        warn "Port 11434 is occupied, but it is not answering as Ollama"
        show_port_owner 11434
        die "Free port 11434 or set up the expected Ollama service"
    fi

    log "Starting the local Ollama server"
    : > "$OLLAMA_LOG"
    nohup ollama serve >> "$OLLAMA_LOG" 2>&1 &
    local ollama_pid=$!
    printf '%s\n' "$ollama_pid" > "$OLLAMA_PID_FILE"

    wait_for_url "$version_url" 60 "Ollama" || {
        tail -n 80 "$OLLAMA_LOG" >&2 || true
        die "Ollama failed to start"
    }
    log "Ollama started with PID ${ollama_pid}"
}

model_is_installed() {
    ollama show "$1" >/dev/null 2>&1
}

pull_model() {
    local model=$1
    local purpose=$2
    log "Pulling/repairing ${purpose} model '${model}' through Ollama"
    ollama pull "$model" || die "Ollama could not pull model '${model}'. Check network, disk space, and model name."
    model_is_installed "$model" || die "Model '${model}' is still unavailable after pull"
}

ensure_model_exists() {
    local model=$1
    local purpose=$2
    if model_is_installed "$model"; then
        log "${purpose} model '${model}' is already installed; skipping download"
    else
        pull_model "$model" "$purpose"
    fi
}

embedding_smoke_test() {
    local output=$1
    curl --fail --silent --show-error --max-time "$MODEL_TIMEOUT_SECONDS" \
        "${RAG_EMBEDDING_BASE_URL%/}/embeddings" \
        -H 'Content-Type: application/json' \
        -d "{\"model\":\"${RAG_EMBEDDING_MODEL}\",\"input\":[\"local startup health check\"]}" \
        > "$output" \
        && grep -q '"embedding"' "$output"
}

chat_smoke_test() {
    local output=$1
    curl --fail --silent --show-error --max-time "$MODEL_TIMEOUT_SECONDS" \
        "${RAG_CHAT_BASE_URL%/}/chat/completions" \
        -H 'Content-Type: application/json' \
        -d "{\"model\":\"${RAG_CHAT_MODEL}\",\"messages\":[{\"role\":\"user\",\"content\":\"Reply with ready\"}],\"temperature\":0}" \
        > "$output" \
        && grep -q '"choices"' "$output"
}

# A manifest existing is not enough to prove a model can actually load. These
# calls exercise the exact OpenAI-compatible routes used by the Java adapters.
# If a smoke test fails, ollama pull verifies/re-fetches required layers once;
# the API is then retested before continuing.
verify_model_apis() {
    local embedding_output chat_output
    embedding_output="$(mktemp "${TMPDIR:-/tmp}/mini-rag-startup.XXXXXX")"
    chat_output="$(mktemp "${TMPDIR:-/tmp}/mini-rag-startup.XXXXXX")"
    TEMP_FILES="${TEMP_FILES} ${embedding_output} ${chat_output}"

    log "Testing embedding model '${RAG_EMBEDDING_MODEL}' through the real API"
    if ! embedding_smoke_test "$embedding_output"; then
        warn "Embedding smoke test failed; attempting one safe model repair"
        pull_model "$RAG_EMBEDDING_MODEL" "embedding"
        embedding_smoke_test "$embedding_output" || {
            cat "$embedding_output" >&2 || true
            die "Embedding API still fails after model repair"
        }
    fi

    log "Testing chat model '${RAG_CHAT_MODEL}' through the real API"
    if ! chat_smoke_test "$chat_output"; then
        warn "Chat smoke test failed; attempting one safe model repair"
        pull_model "$RAG_CHAT_MODEL" "chat"
        chat_smoke_test "$chat_output" || {
            cat "$chat_output" >&2 || true
            die "Chat API still fails after model repair"
        }
    fi
    log "Both local model APIs passed"
}

validate_project_layout() {
    local required_path
    for required_path in \
        "pom.xml" \
        "rag-application/pom.xml" \
        "rag-application/src/main/resources/application.yml"; do
        [[ -f "${SCRIPT_DIR}/${required_path}" ]] \
            || die "Project is incomplete: missing ${required_path}"
    done
}

build_and_verify_jar() {
    local jar_listing
    log "Running clean Maven build and all tests"
    if ! (cd "$SCRIPT_DIR" && mvn clean package); then
        # A transient repository/metadata failure can leave Maven with stale
        # resolution state. One forced-update retry is safe: it does not delete
        # source, databases, or the entire ~/.m2 cache.
        warn "Normal Maven build failed; retrying once with dependency metadata refresh (-U)"
        (cd "$SCRIPT_DIR" && mvn -U clean package)
    fi

    [[ -s "$JAR_PATH" ]] || die "Build succeeded but executable JAR is missing: ${JAR_PATH}"
    command_exists jar || die "The JDK jar tool is missing even though Java was found"

    # A normal library JAR could exist yet be non-executable. Spring Boot's
    # repackaged archive must contain BOOT-INF and the application class.
    jar_listing="$(mktemp "${TMPDIR:-/tmp}/mini-rag-jar.XXXXXX")"
    TEMP_FILES="${TEMP_FILES} ${jar_listing}"
    jar tf "$JAR_PATH" > "$jar_listing"
    grep -q '^BOOT-INF/' "$jar_listing" \
        || die "JAR is not a Spring Boot executable archive"
    grep -q 'dev/learning/rag/app/MiniRagApplication.class' "$jar_listing" \
        || die "JAR does not contain MiniRagApplication"
    log "Executable JAR verified: ${JAR_PATH}"
}

validate_port() {
    if ! [[ "$SERVER_PORT" =~ ^[0-9]+$ ]] \
       || (( SERVER_PORT < 1 || SERVER_PORT > 65535 )); then
        die "SERVER_PORT must be an integer between 1 and 65535"
    fi
}

app_health_url() {
    printf 'http://127.0.0.1:%s/actuator/health\n' "$SERVER_PORT"
}

existing_app_is_healthy() {
    local output
    output="$(curl --fail --silent --show-error --max-time 3 "$(app_health_url)" 2>/dev/null || true)"
    [[ "$output" == *'"status":"UP"'* ]]
}

check_application_port() {
    if existing_app_is_healthy; then
        log "A healthy application is already running on port ${SERVER_PORT}; no duplicate will be started"
        return 2
    fi

    if command_exists lsof && lsof -nP -iTCP:"$SERVER_PORT" -sTCP:LISTEN >/dev/null 2>&1; then
        warn "Port ${SERVER_PORT} is occupied by another process"
        show_port_owner "$SERVER_PORT"
        die "Choose another port, for example: SERVER_PORT=8081 ./start-local.sh"
    fi
    return 0
}

wait_for_application() {
    local elapsed=0
    while (( elapsed < STARTUP_TIMEOUT_SECONDS )); do
        if existing_app_is_healthy; then
            return 0
        fi
        if [[ -n "$APP_PID" ]] && ! kill -0 "$APP_PID" 2>/dev/null; then
            warn "Spring Boot exited before becoming healthy"
            tail -n 120 "$APP_LOG" >&2 || true
            return 1
        fi
        sleep 1
        elapsed=$((elapsed + 1))
    done
    warn "Spring Boot did not become healthy within ${STARTUP_TIMEOUT_SECONDS}s"
    tail -n 120 "$APP_LOG" >&2 || true
    return 1
}

start_application() {
    : > "$APP_LOG"

    if [[ "$MODE" == "background" ]]; then
        log "Starting Spring Boot in the background"
        nohup java -jar "$JAR_PATH" >> "$APP_LOG" 2>&1 &
        APP_PID=$!
        printf '%s\n' "$APP_PID" > "$APP_PID_FILE"
    else
        log "Starting Spring Boot; logs are also written to ${APP_LOG}"
        java -jar "$JAR_PATH" >> "$APP_LOG" 2>&1 &
        APP_PID=$!
        printf '%s\n' "$APP_PID" > "$APP_PID_FILE"
    fi
    STARTED_APP=true

    wait_for_application || die "Application startup validation failed"

    # A health response at one instant is not enough if the JVM immediately
    # crashes afterward. Give it a short stability window, then check process
    # liveness and health once more before reporting success.
    sleep 3
    kill -0 "$APP_PID" 2>/dev/null || {
        tail -n 120 "$APP_LOG" >&2 || true
        die "Application exited during the post-start stability check"
    }
    existing_app_is_healthy || {
        tail -n 120 "$APP_LOG" >&2 || true
        die "Application health regressed during the post-start stability check"
    }

    log "SUCCESS: all prerequisites, model APIs, build checks, and Spring health checks passed"
    log "Application: http://127.0.0.1:${SERVER_PORT}"
    log "Health:      $(app_health_url)"
    log "PID:         ${APP_PID}"
    log "Log:         ${APP_LOG}"

    if [[ "$MODE" == "foreground" ]]; then
        log "Attached mode is active. Press Ctrl+C to stop Spring Boot."
        tail -n 40 -f "$APP_LOG" &
        TAIL_PID=$!
        wait "$APP_PID"
    fi
}

log "Mini RAG Engine local startup diagnosis"
log "Project: ${SCRIPT_DIR}"

# Make all relative application paths (especially ./data/rag.db) deterministic
# even when the script was invoked from another directory.
cd "$SCRIPT_DIR"

ensure_curl
validate_local_model_configuration
check_free_disk_space
ensure_java
ensure_maven
ensure_ollama_cli
ensure_ollama_server
ensure_model_exists "$RAG_EMBEDDING_MODEL" "embedding"
ensure_model_exists "$RAG_CHAT_MODEL" "chat"
verify_model_apis
validate_project_layout
build_and_verify_jar

if [[ "$MODE" == "check-only" ]]; then
    log "SUCCESS: prerequisites, model APIs, tests, and executable JAR are ready"
    exit 0
fi

validate_port
if check_application_port; then
    start_application
else
    # check_application_port returns 2 only for an already healthy instance.
    log "SUCCESS: project diagnostics passed and the existing application is healthy"
fi
