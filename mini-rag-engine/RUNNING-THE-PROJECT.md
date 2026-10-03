# Running the Mini RAG Engine on macOS, Linux, and Windows

This is the operating-system setup guide for someone opening the project for the first time. It explains what must exist on the machine, what the startup scripts install automatically, how to start and stop the application, and how to prove that the complete RAG flow works.

For examples of what to build with the running application, continue with [USE-CASES-AND-SETUP.md](USE-CASES-AND-SETUP.md).

## 1. Choose the correct launcher

| Operating system | Recommended launcher | Automatic prerequisite repair | Supported baseline |
|---|---|---|---|
| macOS | `./start-local.sh` | Yes, if Homebrew is already installed | macOS 14+ on Intel or Apple Silicon (current Ollama requirement) |
| Linux | `./start-local.sh` | No; install the listed tools first | A maintained x64/ARM64 distribution with Java 21 and Ollama support |
| Windows | `.\start-local.cmd` | Yes, through WinGet | Windows 10/11 x64, PowerShell 5.1+ |
| Windows with WSL | `./start-local.sh` inside WSL | Treated as Linux; no automatic package installation | WSL2 with a supported Linux distribution |

The Java application is cross-platform. The separate launchers exist because installation, process management, port inspection, paths, line endings, and background execution differ between Unix-like systems and Windows.

Windows 7, Windows 8, and 32-bit Windows are not supported. The controlling limitations are Java 21, current Ollama releases, and WinGet—not Spring Boot application code.

## 2. What runs on the machine

The default local setup contains only three runtime pieces:

```text
API caller or browser
        |
        | HTTP :8080
        v
Mini RAG Engine (Java/Spring Boot)
        |                         |
        | JDBC                    | HTTP :11434
        v                         v
./data/rag.db                  Ollama
                               |-- embeddinggemma
                               `-- llama2
```

- Spring Boot provides the document, search, and question APIs.
- SQLite is embedded through the Java JDBC dependency. No database server or SQLite installation is required.
- Ollama runs both local models. No cloud model account or API key is required.
- `embeddinggemma` converts document passages and questions into vectors.
- `llama2` turns retrieved evidence into a written answer.

Docker, Kubernetes, Node.js, Python, Oracle infrastructure, and a cloud database are not required.

## 3. Requirements and first-run downloads

| Requirement | Why it is needed | Installed automatically? |
|---|---|---|
| Java JDK 21+ | Compiles and runs the application | macOS with Homebrew; Windows with WinGet; not Linux |
| Maven 3.9+ | Builds all modules and runs tests | macOS with Homebrew; Windows uses bundled `mvnw.cmd`; not Linux |
| Ollama | Serves the embedding and chat models locally | macOS with Homebrew; Windows with WinGet; not Linux |
| `curl` | Unix startup health checks and manual API examples | Normally preinstalled on macOS; install manually on Linux |
| Bash | Runs `start-local.sh` | Preinstalled on macOS/Linux/WSL |
| WinGet | Installs/repairs Windows prerequisites | Normally supplied by Microsoft App Installer |
| Free disk | Stores models, Maven dependencies, build output, and SQLite data | Keep at least 5 GiB free; more is safer |

Internet access is required during the first setup to download missing tools, Maven dependencies, and Ollama model files. Once those artifacts are cached, normal answering is local and does not require a hosted AI service.

Use authoritative installers when a prerequisite must be installed manually:

| Software | Official installation source |
|---|---|
| Java 21 JDK | [Eclipse Temurin 21 releases](https://adoptium.net/temurin/releases?version=21) |
| Maven | [Apache Maven installation guide](https://maven.apache.org/install.html) |
| Ollama | [Ollama downloads for macOS, Linux, and Windows](https://ollama.com/download) |
| WinGet | [Microsoft WinGet documentation](https://learn.microsoft.com/windows/package-manager/) |

Prefer the operating system's trusted package-management process where organizational policy requires it. After any installation, verify the executable and version from a new terminal rather than assuming the installer updated the current shell.

The default models can require several gigabytes and meaningful RAM. If `llama2` is too slow for the machine, select a smaller Ollama chat model through `RAG_CHAT_MODEL`. The replacement must support chat; do not substitute a chat model for the embedding model.

## 4. macOS setup

### 4.1 Recommended automatic path

The macOS launcher can install or repair Java, Maven, and Ollama only when Homebrew is already installed. It deliberately does not install Homebrew by executing a remote script.

Open Terminal and run:

```bash
cd /path/to/mini-rag-engine
chmod +x start-local.sh mvnw
./start-local.sh
```

The launcher will:

1. Check Java 21, Maven 3.9+, `curl`, and Ollama.
2. Use Homebrew to repair missing or outdated supported tools.
3. Start the Ollama service if required.
4. Pull only missing model files.
5. Call both real model endpoints.
6. Build all Maven modules and execute tests.
7. Verify the executable Spring Boot JAR.
8. Start the application and wait for health status `UP`.

### 4.2 When Homebrew is not installed

Choose one of these paths:

- Install Homebrew separately using its official instructions, then run the launcher.
- Manually install a Java 21 JDK, Maven 3.9+, Ollama, and `curl`, then run `./start-local.sh --no-install`.

Verify a manual installation:

```bash
java -version
mvn -version
curl --version
ollama --version
```

Java must report version 21 or newer. Maven must report 3.9 or newer and must itself be using Java 21+.

If Homebrew Java exists but is not active, a typical temporary shell configuration is:

```bash
export JAVA_HOME="$(/usr/libexec/java_home -v 21)"
export PATH="$JAVA_HOME/bin:$PATH"
```

## 5. Linux setup

Linux execution is supported, but `start-local.sh` intentionally does not guess whether to use `apt`, `dnf`, `yum`, `zypper`, `pacman`, Snap, or another package manager. Install prerequisites using the supported process for the distribution.

Install these items before running the project:

- A complete Java 21 JDK, not only a JRE.
- Maven 3.9 or newer. Some older distribution repositories provide Maven 3.8; verify the actual version.
- `curl`.
- Ollama for Linux.
- Optional but recommended: `lsof`, which lets the script identify processes using ports 8080 or 11434.

Then verify:

```bash
java -version
mvn -version
curl --version
ollama --version
```

Start the project:

```bash
cd /path/to/mini-rag-engine
chmod +x start-local.sh mvnw
./start-local.sh --no-install
```

`--no-install` is recommended on Linux because it states the intended behavior explicitly. If a prerequisite is missing, the script stops and names it; it will not modify the Linux package database.

For a headless Linux machine, use background mode:

```bash
./start-local.sh --no-install --background
```

The API binds to the configured Spring Boot address and port. V1 has no authentication or TLS; do not expose it directly to an untrusted network. Put authentication, TLS, request limits, and network restrictions in front of it before shared or remote use.

## 6. Native Windows setup

### 6.1 Recommended path

Open Command Prompt or PowerShell:

```powershell
cd C:\path\to\mini-rag-engine
.\start-local.cmd
```

The command launcher calls `start-local.ps1`. Its execution-policy bypass applies only to that process and does not change the permanent machine or user policy.

The Windows launcher:

- Finds a valid Java 21+ JDK or installs/repairs `EclipseAdoptium.Temurin.21.JDK` with WinGet.
- Finds a working Ollama CLI or installs/repairs `Ollama.Ollama` with WinGet.
- Uses the bundled `mvnw.cmd`; no global Maven installation is required.
- Uses `Get-NetTCPConnection`, `Get-Process`, `Start-Process`, and `Invoke-RestMethod` instead of Unix utilities.
- Stores Windows-specific logs and PID files under `.run\`.

WinGet installations can trigger a Windows elevation/UAC prompt. If WinGet is missing, install Microsoft App Installer or manually install Java 21 and Ollama.

### 6.2 Diagnose without automatic installation

```powershell
.\start-local.cmd -NoInstall -CheckOnly
```

This reports what is missing without changing installed software or starting Spring Boot.

### 6.3 Direct PowerShell invocation

```powershell
powershell.exe -NoLogo -NoProfile -ExecutionPolicy Bypass -File .\start-local.ps1
```

PowerShell 7 users may replace `powershell.exe` with `pwsh`.

## 7. Launcher modes on every platform

| Purpose | macOS/Linux | Windows |
|---|---|---|
| Normal foreground startup | `./start-local.sh` | `.\start-local.cmd` |
| Build and diagnose only | `./start-local.sh --check-only` | `.\start-local.cmd -CheckOnly` |
| Start and return to shell | `./start-local.sh --background` | `.\start-local.cmd -Background` |
| Disable tool installation | `./start-local.sh --no-install` | `.\start-local.cmd -NoInstall` |
| Show help | `./start-local.sh --help` | `.\start-local.cmd -Help` |

Foreground mode is best while learning because application output remains visible. Press `Ctrl+C` to stop the Spring Boot process started by that launcher. Ollama intentionally remains running because it is a shared local service.

## 8. Configuration overrides

The defaults are:

| Setting | Default |
|---|---|
| Application URL | `http://localhost:8080` |
| Ollama/OpenAI-compatible URL | `http://localhost:11434/v1` |
| Embedding model | `embeddinggemma` |
| Chat model | `llama2` |
| SQLite file | `./data/rag.db` |

macOS/Linux example:

```bash
SERVER_PORT=8081 \
RAG_CHAT_MODEL=my-chat-model \
RAG_EMBEDDING_MODEL=my-embedding-model \
./start-local.sh
```

Windows PowerShell example:

```powershell
$env:SERVER_PORT = '8081'
$env:RAG_CHAT_MODEL = 'my-chat-model'
$env:RAG_EMBEDDING_MODEL = 'my-embedding-model'
.\start-local.cmd
```

Changing the chat model does not require re-indexing. Changing the embedding model does require a new SQLite database or re-indexing, because vectors produced by different embedding models cannot be compared safely.

Use a separate database while experimenting:

```bash
RAG_DATABASE_PATH=./data/experiment.db ./start-local.sh
```

```powershell
$env:RAG_DATABASE_PATH = '.\data\experiment.db'
.\start-local.cmd
```

## 9. Prove that the application works

### 9.1 Health check

macOS/Linux:

```bash
curl --fail http://localhost:8080/actuator/health
```

Windows PowerShell:

```powershell
Invoke-RestMethod http://localhost:8080/actuator/health
```

Expected status: `UP`.

### 9.2 Upload a text-based PDF

macOS/Linux:

```bash
curl --fail -F 'file=@/absolute/path/to/document.pdf' \
  http://localhost:8080/api/documents
```

Windows 10/11 includes `curl.exe`; call it explicitly to avoid the historical PowerShell alias:

```powershell
curl.exe --fail -F "file=@C:\absolute\path\to\document.pdf" `
  http://localhost:8080/api/documents
```

The returned JSON contains the document ID, filename, checksum, page count, and creation time.

### 9.3 Inspect semantic retrieval

macOS/Linux:

```bash
curl --fail -X POST http://localhost:8080/api/search \
  -H 'Content-Type: application/json' \
  -d '{"question":"What are the retry rules?","topK":5,"minimumScore":0.20}'
```

Windows PowerShell:

```powershell
$body = @{
  question = 'What are the retry rules?'
  topK = 5
  minimumScore = 0.20
} | ConvertTo-Json

Invoke-RestMethod -Method Post `
  -Uri http://localhost:8080/api/search `
  -ContentType 'application/json' `
  -Body $body
```

Relevant passages, filenames, page numbers, ranks, and scores should appear. Diagnose this endpoint before blaming the chat model for a poor answer.

### 9.4 Ask a grounded question

macOS/Linux:

```bash
curl --fail -X POST http://localhost:8080/api/questions \
  -H 'Content-Type: application/json' \
  -d '{"question":"What are the retry rules?"}'
```

Windows PowerShell:

```powershell
$body = @{ question = 'What are the retry rules?' } | ConvertTo-Json
Invoke-RestMethod -Method Post `
  -Uri http://localhost:8080/api/questions `
  -ContentType 'application/json' `
  -Body $body
```

The response contains the written answer, the chat model name, and the exact retrieved sources used as evidence.

## 10. Manual build and startup

Use this path when debugging one layer at a time.

macOS/Linux:

```bash
ollama serve
ollama pull embeddinggemma
ollama pull llama2
./mvnw clean package
java -jar rag-application/target/rag-application-0.1.0-SNAPSHOT.jar
```

The automated Bash launcher currently validates global Maven 3.9+, while the manual build can use the bundled `./mvnw` wrapper.

Windows:

```powershell
ollama serve
ollama pull embeddinggemma
ollama pull llama2
.\mvnw.cmd clean package
java -jar .\rag-application\target\rag-application-0.1.0-SNAPSHOT.jar
```

Do not start a second `ollama serve` if `http://localhost:11434/api/version` already responds.

## 11. Files created at runtime

| Path | Contents | Safe handling |
|---|---|---|
| `data/rag.db` | Documents, chunks, embeddings, and model metadata | Preserve it to retain indexed data; back it up while the app is stopped |
| `.run/application*.log` | Spring Boot output | Inspect during startup/API failures |
| `.run/ollama*.log` | Ollama output when started by the launcher | Inspect when models do not load |
| `.run/*.pid` | Process identifiers | Diagnostic metadata, not application data |
| `*/target/` | Maven build output | Recreated by the build |
| `~/.m2/` | Maven dependency cache | Shared Maven cache outside the repository |
| Ollama model directory | Model packages managed by Ollama | Manage with `ollama list`, `pull`, and `rm` |

The launchers never delete a database, remove a model, or kill an unknown process merely because it owns a required port.

## 12. Common failures

| Symptom | Meaning | Resolution |
|---|---|---|
| Java version is below 21 | Wrong JDK is active | Correct `JAVA_HOME`/`PATH`, or allow the launcher to install Java 21 |
| Maven is below 3.9 on macOS/Linux | Bash launcher rejects the global Maven | Upgrade Maven or use the manual `./mvnw` build path |
| `connection refused` on 11434 | Ollama is not running | Start Ollama and re-run the launcher |
| Port 11434 is occupied but Ollama does not answer | Another process owns Ollama's expected port | Identify and reconfigure/stop that process; the script will not kill it |
| Port 8080 is occupied | Another application is using the default Spring port | Set `SERVER_PORT=8081` or the PowerShell equivalent |
| Model pull fails | Network, disk, or model-name problem | Check connectivity/free disk, then run `ollama pull MODEL` |
| Embedding test fails | Wrong model type or broken local model | Use an embedding-capable model and inspect Ollama logs |
| PDF upload returns HTTP 400 | Empty, invalid, encrypted, scanned, or textless PDF | Use a text-based PDF; V1 does not perform OCR |
| Search returns no useful passages | Threshold too high, weak chunking, wrong content, or model mismatch | Try `/api/search` with a lower `minimumScore`; confirm the same embedding model was used for indexing and queries |
| HTTP 502 from question/search | Model endpoint failed or returned invalid output | Check Ollama, model names, endpoint URL, timeout, and `.run` logs |
| Existing data becomes unsearchable after model change | Old and new embedding spaces differ | Point `RAG_DATABASE_PATH` to a new file and re-index documents |

## 13. Using a model provider other than local Ollama

The Java application can call another server implementing `/v1/embeddings` and `/v1/chat/completions`, but the fail-safe startup scripts intentionally manage local Ollama only. For another provider:

1. Build with `./mvnw clean package` or `.\mvnw.cmd clean package`.
2. Set `RAG_CHAT_BASE_URL`, `RAG_CHAT_API_KEY`, `RAG_CHAT_MODEL`.
3. Set `RAG_EMBEDDING_BASE_URL`, `RAG_EMBEDDING_API_KEY`, `RAG_EMBEDDING_MODEL`.
4. Set a fresh `RAG_DATABASE_PATH` when changing embedding models.
5. Start the JAR manually.

Never commit real API keys. This V1 does not include a secrets manager, authentication, multi-tenancy, or a production deployment envelope.
