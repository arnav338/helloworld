# Mini RAG Engine

A learning-first, local document question-answering system built with Java 21. It extracts text from PDFs, creates embeddings, persists chunks and vectors in SQLite, performs cosine-similarity retrieval in Java, and asks a chat model to answer only from retrieved evidence.

The project intentionally does **not** use LangChain, Spring AI, an Oracle service, Kubernetes, or a hosted database. The important mechanics stay visible in ordinary Java.

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

## Prerequisites

Required:

- Java 21
- Maven 3.9+
- Ollama or another OpenAI-compatible endpoint
- One chat model and one embedding model

Not required:

- A separate SQLite installation; `sqlite-jdbc` embeds it.
- Docker.
- A cloud account or API key when using local Ollama.

Verify tools:

```bash
java -version
mvn -version
ollama --version
curl http://localhost:11434/api/version
```

Install models for the default configuration:

```bash
ollama pull embeddinggemma
ollama pull llama2
```

`llama2` is the default because it already exists on the current workstation. Change `RAG_CHAT_MODEL` to any suitable locally installed chat model. The chat and embedding model do not need to be from the same model family.

## Build and test

From this directory:

```bash
mvn clean test
mvn package
```

Maven builds every module in the order declared by the root `pom.xml`. The resulting executable is:

```text
rag-application/target/rag-application-0.1.0-SNAPSHOT.jar
```

## Start locally

Make sure Ollama is running, then:

```bash
java -jar rag-application/target/rag-application-0.1.0-SNAPSHOT.jar
```

Or run through Maven:

```bash
mvn -pl rag-application -am spring-boot:run
```

Verify the application without invoking a model:

```bash
curl http://localhost:8080/actuator/health
```

The SQLite file is created automatically at `./data/rag.db` relative to the directory from which the application starts.

## First end-to-end test

Upload a text-based PDF:

```bash
curl -sS -F 'file=@/absolute/path/to/document.pdf' \
  http://localhost:8080/api/documents
```

List indexed documents:

```bash
curl -sS http://localhost:8080/api/documents
```

Inspect semantic retrieval without spending chat-model time:

```bash
curl -sS -X POST http://localhost:8080/api/search \
  -H 'Content-Type: application/json' \
  -d '{"question":"How are retries handled?","topK":5,"minimumScore":0.20}'
```

Ask for a grounded answer:

```bash
curl -sS -X POST http://localhost:8080/api/questions \
  -H 'Content-Type: application/json' \
  -d '{"question":"How are retries handled?"}'
```

Delete a document using the returned UUID:

```bash
curl -X DELETE http://localhost:8080/api/documents/DOCUMENT_UUID
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

