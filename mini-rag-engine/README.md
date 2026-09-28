# Mini RAG Engine

A learning-first, local document question-answering system built with Java 21. It extracts text from PDFs, creates embeddings, persists chunks and vectors in SQLite, performs cosine-similarity retrieval in Java, and asks a chat model to answer only from retrieved evidence.

The project intentionally does **not** use LangChain, Spring AI, an Oracle service, Kubernetes, or a hosted database. The important mechanics stay visible in ordinary Java.

If you understand conventional Spring Controller -> Service -> Repository applications, begin with [BACKEND-DEVELOPER-GUIDE.md](BACKEND-DEVELOPER-GUIDE.md). It maps every RAG component to familiar backend layers, traces both request flows, and explains exactly where V1/V2 implementations can be selected.

## Current capabilities

- Upload and index text-based PDFs.
- Preserve filename, page number, and chunk provenance.
- Prevent duplicate indexing with SHA-256 checksums.
- Generate embeddings through an OpenAI-compatible HTTP endpoint.
- Persist everything in a local SQLite database.
- Calculate cosine similarity and top-k ranking in Java.
- Inspect retrieval independently through `/api/search`.
- Generate evidence-grounded answers through `/api/questions`.
- Return citations and similarity scores with each answer.
- Change chat and embedding endpoints independently through configuration.

## Architecture

The parent Maven project builds eight modules in dependency order:

| Module | Role | Knows about infrastructure? |
|---|---|---:|
| `rag-model` | Immutable data exchanged between layers | No |
| `rag-provider-api` | Chat and embedding extension contracts | No |
| `rag-store-api` | Persistence extension contract | No |
| `rag-core` | Chunking, cosine similarity, retrieval, prompt, orchestration | No |
| `rag-provider-openai-compatible` | Plain HTTP model adapter | Yes: HTTP/JSON |
| `rag-store-sqlite` | File-backed persistence adapter | Yes: JDBC/SQLite |
| `rag-document-pdf` | PDFBox page-text extraction adapter | Yes: PDFBox |
| `rag-application` | Spring Boot wiring and REST API | Yes: Spring/HTTP |

Dependency direction points inward. Core logic depends on interfaces, never on Ollama, SQLite, PDFBox, or Spring. Read every module's `README.md` before changing its public contract.

## Does Ollama need to be running?

Yes, for the default local setup. There are three separate requirements:

1. **Ollama must be installed** so the `ollama` command exists.
2. **The Ollama server must be running** at `http://localhost:11434`.
3. **Both configured models must be downloaded**: one embedding model and one chat model.

### What "models must be available locally" means

This does **not** mean that you must install or register with two additional AI providers. Ollama is the only AI runtime/service used by the local setup.

Think of the setup as:

```text
This Spring Boot application
        |
        | HTTP requests to localhost:11434
        v
Ollama (one locally running program)
        |
        +-- embeddinggemma model files: text -> vectors
        |
        +-- llama2 model files: prompt -> written answer
```

An Ollama model is a downloaded package of model weights and metadata. The `ollama pull` command obtains that package from Ollama's model library and stores it in Ollama's local model storage. Ollama manages those files; this project does not need their filesystem paths.

```bash
# Download the local search/embedding model.
ollama pull embeddinggemma

# Download the local answer-generation/chat model.
ollama pull llama2

# Show models already stored on this machine.
ollama list
```

After the models have been downloaded:

- The application calls only `http://localhost:11434`.
- Document text and questions do not need to be sent to a cloud AI provider.
- No model-provider API key or cloud account is required.
- Ollama loads the appropriate local model when an embedding or chat request arrives.
- Internet access is not normally required while running the application.

Internet access is needed initially to install/download Ollama and model packages. Maven also needs internet access on its first build to download Java libraries; those libraries are then cached locally. These are setup downloads, not remote runtime services used for answering questions.

Two models are used because they perform different jobs:

| Local Ollama model | Job | Output |
|---|---|---|
| `embeddinggemma` | Converts PDF chunks and questions into comparable numeric representations | A vector such as `[0.021, -0.184, ...]` |
| `llama2` | Reads the question and retrieved PDF text and writes the response | Natural-language text |

Both models run behind the same Ollama process. You do not start one server per model.

The Spring Boot process can start and `/actuator/health` can report `UP` without Ollama because model connections are made only when needed. However:

| Operation | Needs Ollama? | Model used |
|---|---:|---|
| Start Spring Boot | No | None |
| Health check | No | None |
| List/delete already indexed documents | No | None |
| Upload/index a PDF | Yes | Embedding model |
| Search indexed documents | Yes | Embedding model |
| Ask for a generated answer | Yes | Embedding model and chat model |

Ollama is not mandatory only if you deliberately choose to configure another server that implements the OpenAI-compatible `/v1/embeddings` and `/v1/chat/completions` APIs. The default project does not depend on such a server.

## Prerequisites

### Required software

| Requirement | Minimum/project value | Why it is needed |
|---|---|---|
| Java JDK | Java 21 | Compiles and runs the Spring Boot application. A JRE alone is insufficient for Maven compilation. |
| Maven | 3.9 or newer | Builds all eight modules, downloads Java dependencies, and runs tests. |
| Ollama | Current local version, or another compatible server | Runs the embedding and chat models locally. |
| `curl` | Any recent version | Used by the README examples to verify services and call the REST API. |
| Free disk space | Several GB | Model files such as `llama2` are large. Exact size depends on the selected models. |

The default application configuration expects:

```text
Ollama URL:       http://localhost:11434/v1
Chat model:       llama2
Embedding model:  embeddinggemma
Application port: 8080
SQLite file:      ./data/rag.db
```

### Not required

- A separate SQLite installation. The `sqlite-jdbc` dependency contains the database engine.
- Docker or Kubernetes.
- An Oracle database or Oracle container image.
- A cloud account or API key when using local Ollama.
- Node.js, Python, LangChain, or Spring AI.

### Verify Java and Maven

Run:

```bash
java -version
mvn -version
```

Expected results:

- `java -version` reports version 21.
- `mvn -version` reports Maven 3.9+ and shows that Maven itself is using Java 21.

If Maven reports an older Java runtime even though Java 21 is installed, correct `JAVA_HOME` before building. On macOS, a temporary fix is:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH="$JAVA_HOME/bin:$PATH"
java -version
mvn -version
```

### Install Ollama when it is missing

On macOS with Homebrew:

```bash
brew install ollama
```

Alternatively, install the Ollama desktop application. Verify the CLI afterward:

```bash
ollama --version
```

If you use a different operating system, install Ollama using its platform-specific package and continue with the same server/model checks below.

## One-time model setup

The application uses different model types for different jobs:

- `embeddinggemma` converts document chunks and questions into numeric vectors for search.
- `llama2` reads the question plus retrieved text and generates the final answer.

Download both default models once:

```bash
ollama pull embeddinggemma
ollama pull llama2
```

Downloads may take several minutes and consume multiple gigabytes. Confirm that both names are present:

```bash
ollama list
```

Expected entries include:

```text
embeddinggemma:latest
llama2:latest
```

The exact displayed tag may include `:latest`; the configuration value `embeddinggemma` or `llama2` resolves that tag automatically.

Important: a chat model is not a replacement for an embedding model. The application needs an embedding-capable model for indexing/search and a chat-capable model for answers.

## Recommended: one-command fail-safe startup

Manual commands remain documented below for learning and troubleshooting, but normal local use should start with:

```bash
cd /Users/arnavmalhotra/IdeaProjects/helloworld/mini-rag-engine
./start-local.sh
```

The script is idempotent: running it again rechecks the environment and skips healthy components instead of blindly reinstalling or redownloading them.

It performs these checks in order:

1. Prevents two copies of the startup script from modifying build/PID state concurrently.
2. Validates Java 21+, Maven 3.9+, `curl`, and the Ollama CLI.
3. On macOS, uses an existing Homebrew installation to install or repair missing/outdated Java, Maven, or Ollama.
4. Starts `ollama serve` when the server is not already available.
5. Checks whether `embeddinggemma` and `llama2` are locally installed.
6. Pulls only a missing model; an already available model skips downloading.
7. Calls the real embedding and chat HTTP endpoints—not merely `ollama list`—to prove both models load and respond.
8. If a model API fails, runs one `ollama pull` repair/verification and retests it.
9. Validates the Maven project layout.
10. Runs `mvn clean package`, including all tests.
11. Verifies that the output is an executable Spring Boot JAR containing `MiniRagApplication`.
12. Refuses to kill an unrelated process if port 8080 or 11434 is occupied.
13. Starts Spring Boot and waits for `/actuator/health` to return `UP`.
14. Prints `SUCCESS` only after all required checks pass.

The default mode stays attached to Spring Boot. Press `Ctrl+C` to stop the application:

```bash
./start-local.sh
```

Start it in the background:

```bash
./start-local.sh --background
```

Run all prerequisite, model, test, and JAR diagnostics without starting Spring Boot:

```bash
./start-local.sh --check-only
```

Prevent automatic Homebrew changes while still running diagnostics:

```bash
./start-local.sh --no-install --check-only
```

Use another application port:

```bash
SERVER_PORT=8081 ./start-local.sh
```

Use different locally installed/pullable Ollama models:

```bash
RAG_CHAT_MODEL=my-chat-model \
RAG_EMBEDDING_MODEL=my-embedding-model \
./start-local.sh
```

Logs and PID files are written beneath `.run/`:

```text
.run/application.log
.run/application.pid
.run/ollama.log
.run/ollama.pid
```

### What a successful script run guarantees

When the script reports `SUCCESS`, it has proved that, at that moment:

- Required local commands satisfy the project's minimum versions.
- The Ollama server responds.
- The configured embedding endpoint returned an embedding.
- The configured chat endpoint returned a completion.
- The complete clean Maven build and all tests passed.
- The executable JAR has the expected Spring Boot contents.
- Spring Boot started successfully and its health endpoint returned `UP`.

No startup script can guarantee that a later hardware failure, loss of disk space, unsupported model, operating-system permission change, or future request-specific bug will never occur. This script therefore fails with diagnostics instead of claiming to handle an unknowable failure.

### Deliberate safety limits

The script does not:

- Delete an existing SQLite database.
- Delete Ollama models as a repair strategy.
- Kill an unknown process using a required port.
- Install Homebrew by piping an internet script into a shell.
- Manage remote/cloud model providers; `start-local.sh` is intentionally for local Ollama.

These limits prevent a convenience command from becoming destructive. If it stops, read the reported reason and `.run` logs rather than bypassing the check.

## Start the application: complete local procedure

This section shows what the automated script performs and is useful when learning or isolating a failure.

Use three terminal windows while learning the project:

- Terminal 1 runs Ollama.
- Terminal 2 runs Spring Boot.
- Terminal 3 sends test requests.

### Step 1: move to the project root

In Terminal 2:

```bash
cd /Users/arnavmalhotra/IdeaProjects/helloworld/mini-rag-engine
```

All remaining Maven and `java -jar` commands should be run from this directory. The relative SQLite path is resolved from the directory where Java starts.

### Step 2: start or verify Ollama

Ollama may already be running if its desktop application is open. First check it:

```bash
curl --fail --silent --show-error http://localhost:11434/api/version
```

A successful response resembles:

```json
{"version":"0.34.3"}
```

If the request reports connection refused, start Ollama in Terminal 1:

```bash
ollama serve
```

Leave that terminal running. If `ollama serve` says port `11434` is already in use, another Ollama process is probably already serving requests; run the version check again instead of starting a duplicate process.

Verify the required models separately:

```bash
ollama list
```

If either default model is absent:

```bash
ollama pull embeddinggemma
ollama pull llama2
```

### Step 3: optionally test Ollama directly

Test the embedding endpoint:

```bash
curl --fail --silent --show-error http://localhost:11434/v1/embeddings \
  -H 'Content-Type: application/json' \
  -d '{"model":"embeddinggemma","input":["Spring dependency injection"]}'
```

The response should contain a `data` array with an `embedding` array of numbers.

Test the chat endpoint:

```bash
curl --fail --silent --show-error http://localhost:11434/v1/chat/completions \
  -H 'Content-Type: application/json' \
  -d '{"model":"llama2","messages":[{"role":"user","content":"Reply with the word ready"}],"temperature":0}'
```

The response should contain `choices[0].message.content`. The first request can be slow because Ollama must load the model into memory.

### Step 4: build and test every module

In Terminal 2, from the project root:

```bash
mvn clean package
```

This command:

1. Deletes previous build output.
2. Compiles all eight modules in dependency order.
3. Runs the unit and adapter tests.
4. Packages the executable Spring Boot JAR.

A successful build ends with:

```text
BUILD SUCCESS
```

The executable is created at:

```text
rag-application/target/rag-application-0.1.0-SNAPSHOT.jar
```

Maven needs internet access on the first build to download dependencies into `~/.m2`. Later builds normally reuse that local cache.

### Step 5: start Spring Boot

Still in Terminal 2:

```bash
java -jar rag-application/target/rag-application-0.1.0-SNAPSHOT.jar
```

Leave this terminal running. Successful startup includes a message similar to:

```text
Started MiniRagApplication
```

The application listens on:

```text
http://localhost:8080
```

The first startup creates the `data` directory and SQLite file automatically:

```text
./data/rag.db
```

Alternative development command:

```bash
mvn -pl rag-application -am spring-boot:run
```

The packaged-JAR command is recommended for the first run because it proves that the complete distributable was built correctly.

### Step 6: verify Spring Boot independently

In Terminal 3:

```bash
curl --fail --silent --show-error http://localhost:8080/actuator/health
```

Expected response:

```json
{"status":"UP"}
```

This proves that Spring Boot and SQLite initialized. It does not prove that the two model endpoints work; that is why Ollama was tested separately.

## First end-to-end test

### 1. Choose a text-based PDF

The PDF must contain selectable text. Image-only/scanned PDFs require OCR, which V1 does not implement. Replace the example path below with a real absolute path.

### 2. Upload and index the PDF

```bash
curl --fail --silent --show-error \
  -F 'file=@/absolute/path/to/document.pdf' \
  http://localhost:8080/api/documents
```

During this request, Spring extracts text, creates chunks, calls `embeddinggemma`, and writes everything to SQLite. The response contains a document UUID. Uploading the exact same bytes again returns the existing document because SHA-256 deduplication is enabled.

### 3. List indexed documents

```bash
curl --fail --silent --show-error \
  http://localhost:8080/api/documents
```

### 4. Inspect retrieval before involving the chat model

Ask a question whose answer appears in the uploaded PDF:

```bash
curl --fail --silent --show-error \
  -X POST http://localhost:8080/api/search \
  -H 'Content-Type: application/json' \
  -d '{"question":"How are retries handled?","topK":5,"minimumScore":0.20}'
```

This endpoint calls only the embedding model. Its response should contain ranked chunks, similarity scores, filenames, and page numbers. If relevant text does not appear here, diagnose retrieval before testing answer generation.

### 5. Generate a grounded answer

```bash
curl --fail --silent --show-error \
  -X POST http://localhost:8080/api/questions \
  -H 'Content-Type: application/json' \
  -d '{"question":"How are retries handled?"}'
```

This endpoint performs retrieval and then sends the question plus retrieved text to `llama2`. It returns the generated answer, model name, and source passages.

### 6. Delete the indexed document

Replace `DOCUMENT_UUID` with the `id` returned by upload/list:

```bash
curl --fail --silent --show-error \
  -X DELETE http://localhost:8080/api/documents/DOCUMENT_UUID
```

SQLite cascade deletion removes the document, its chunks, and its vectors.

## Stop and restart

Stop the Spring Boot application by pressing `Ctrl+C` in Terminal 2. Stop a terminal-started Ollama server with `Ctrl+C` in Terminal 1. If Ollama is managed by its desktop application, it may continue running normally.

The indexed data survives application restarts in `data/rag.db`. Starting the application again from the same directory reuses that file.

To experiment with a fresh database without deleting anything, provide another path:

```bash
RAG_DATABASE_PATH=./data/experiment.db \
java -jar rag-application/target/rag-application-0.1.0-SNAPSHOT.jar
```

## What happens during indexing

1. The controller receives multipart bytes.
2. `DocumentIndexingService` checks the filename and `%PDF-` signature.
3. SHA-256 detects an identical previously indexed file.
4. PDFBox extracts one `DocumentPage` per physical page.
5. `ParagraphChunker` creates overlapping page-local chunks.
6. The embedding adapter sends bounded batches to `/v1/embeddings`.
7. The service validates vector count, dimension, and finite values.
8. SQLite writes the document, chunks, and vectors in one transaction.

If any step before step 8 fails, no partial document is stored.

## What happens during a question

1. The same embedding model embeds the question.
2. `Retriever` loads stored vectors created by that model.
3. `CosineSimilarity` scores every compatible chunk.
4. Results below `minimumScore` are removed.
5. Remaining results are sorted and limited to `topK`.
6. `PromptBuilder` labels passages as untrusted evidence.
7. The chat adapter calls `/v1/chat/completions` with temperature zero.
8. The API returns the answer and the exact retrieved sources.

Use `/api/search` first when an answer is bad. If retrieval is wrong, changing the prompt cannot repair it.

## Configuration reference

Every setting in `rag-application/src/main/resources/application.yml` has an environment-variable override.

| Variable | Default | Meaning |
|---|---|---|
| `RAG_CHAT_BASE_URL` | `http://localhost:11434/v1` | Chat endpoint root |
| `RAG_CHAT_API_KEY` | `ollama` | Bearer value; Ollama ignores it |
| `RAG_CHAT_MODEL` | `llama2` | Chat model identifier |
| `RAG_CHAT_TIMEOUT` | `120s` | One chat request timeout |
| `RAG_EMBEDDING_BASE_URL` | `http://localhost:11434/v1` | Embedding endpoint root |
| `RAG_EMBEDDING_API_KEY` | `ollama` | Embedding endpoint bearer value |
| `RAG_EMBEDDING_MODEL` | `embeddinggemma` | Embedding model identifier |
| `RAG_EMBEDDING_TIMEOUT` | `120s` | One embedding request timeout |
| `RAG_DATABASE_PATH` | `./data/rag.db` | SQLite file |
| `RAG_CHUNK_MAX_CHARACTERS` | `2400` | Maximum characters per chunk |
| `RAG_CHUNK_OVERLAP_CHARACTERS` | `300` | Context repeated between chunks |
| `RAG_TOP_K` | `5` | Default passages returned |
| `RAG_MINIMUM_SCORE` | `0.25` | Default cosine cutoff |
| `RAG_EMBEDDING_BATCH_SIZE` | `16` | Chunks per embedding request |
| `SERVER_PORT` | `8080` | Application HTTP port |

Example model change:

```bash
RAG_CHAT_MODEL=my-local-chat-model \
RAG_EMBEDDING_MODEL=my-local-embedding-model \
java -jar rag-application/target/rag-application-0.1.0-SNAPSHOT.jar
```

## Plug in another OpenAI-compatible server

This is configuration-only if the server implements `/v1/chat/completions` and `/v1/embeddings`.

1. Start the server and note its base URL.
2. Confirm both endpoints with `curl` or its documentation.
3. Obtain a key if the server requires one.
4. Choose a chat model identifier and an embedding model identifier.
5. Set the four provider variables independently.
6. Delete `data/rag.db` or use a new database path if the embedding model changed.
7. Start the application and re-index documents.

```bash
export RAG_CHAT_BASE_URL=http://localhost:9000/v1
export RAG_CHAT_MODEL=my-chat-model
export RAG_CHAT_API_KEY=local-key
export RAG_EMBEDDING_BASE_URL=http://localhost:9000/v1
export RAG_EMBEDDING_MODEL=my-embedding-model
export RAG_EMBEDDING_API_KEY=local-key
```

Changing only the chat model does not require re-indexing. Changing the embedding model does because embeddings from different vector spaces are not comparable.

## Plug in a non-compatible model API

1. Create a new Maven module.
2. Depend on `rag-provider-api`, not `rag-core` or `rag-application`.
3. Implement `ChatModel`, `EmbeddingModel`, or both.
4. Translate the neutral request into the provider's native request.
5. Validate output counts, dimensions, finite values, HTTP statuses, and timeouts.
6. Add contract tests using a local stub server.
7. Add the module to the root `pom.xml` and application dependencies.
8. Change only the bean in `RagConfiguration`.
9. Document the new environment variables and re-index requirements.

Never place provider-specific JSON or SDK classes in `rag-core`.

## Plug in another database

1. Create a new module depending on `rag-store-api` and `rag-model`.
2. Implement every `VectorStore` operation.
3. Preserve atomic document-plus-chunk writes and cascade deletion.
4. Store embedding model and dimensions beside vectors.
5. Add persistence, restart, rollback, and deletion tests.
6. Add the module to the Maven reactor and application dependencies.
7. Replace the `vectorStore` bean in `RagConfiguration`.

For pgvector at scale, extend the abstraction deliberately so ranking can happen in the database. Do not load millions of vectors into Java merely to preserve the V1 interface.

## Maintenance rules

- Keep model objects immutable and validate at construction.
- Keep infrastructure imports out of `rag-core`.
- Add tests before changing cosine math or chunk boundary rules.
- Treat stored embedding model and dimension as data compatibility metadata.
- Add a migration strategy before changing the SQLite schema in a released version.
- Never log API keys, authorization headers, entire prompts, or document contents.
- Pin dependency versions; review upgrades module by module.
- Run `mvn clean test` from the root before committing.

## Troubleshooting

### Connection refused on port 11434

Ollama is not running or the URL is wrong:

```bash
ollama serve
curl http://localhost:11434/api/version
```

### HTTP 404 from model provider

The base URL normally needs `/v1`, or the provider is not OpenAI-compatible. Verify `RAG_CHAT_BASE_URL` and `RAG_EMBEDDING_BASE_URL` independently.

### Model not found

```bash
ollama list
ollama pull embeddinggemma
```

Ensure the configured model name exactly matches `ollama list`.

### Existing documents never appear in search after changing embedding model

Old vectors are intentionally ignored because their stored model name differs. Re-index into a clean database:

```bash
RAG_DATABASE_PATH=./data/new-model.db java -jar rag-application/target/rag-application-0.1.0-SNAPSHOT.jar
```

### PDF contains no extractable text

It is probably scanned or image-only. V1 does not perform OCR. Try a text-based PDF or add a future OCR adapter.

### Search works but answer is wrong

Inspect `/api/search`. If sources are correct, examine prompt construction and chat-model capability. If sources are wrong, tune chunk size, overlap, top-k, minimum score, dataset quality, or embedding model.

### SQLite database is locked

Stop duplicate application processes and confirm only one process is writing the same local file. WAL improves normal concurrency but SQLite is not intended for high-write distributed deployment.

### Port 8080 is already in use

```bash
SERVER_PORT=8081 java -jar rag-application/target/rag-application-0.1.0-SNAPSHOT.jar
```

## Learning map

Search and study these topics while reading the implementation: RAG indexing/query pipelines, embeddings, vector spaces, cosine similarity, exact k-nearest-neighbor search, chunk overlap, prompt injection, context windows, ports-and-adapters architecture, dependency inversion, Java records, defensive copying, JDBC transactions, SQLite WAL, HTTP JSON APIs, and Spring Boot configuration properties.

The original design material is retained under `project-plan/`.
