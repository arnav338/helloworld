# Mini RAG Engine for a Spring Backend Developer

This guide explains the project using the same mental model as a normal Spring MVC application. You do not need prior AI knowledge. Treat embedding and chat providers as two external backend services, similar to a payment gateway or notification API.

## 1. Translate the architecture into familiar Spring terms

| Familiar backend concept | Class or module in this project | Responsibility |
|---|---|---|
| Application bootstrap | `MiniRagApplication` | Starts Spring Boot and component/configuration discovery. |
| Controller | `RagController` | Accepts HTTP input, validates it, calls a use case, and returns DTOs. |
| Global exception mapper | `ApiExceptionHandler` | Converts Java exceptions into stable HTTP error responses. |
| Service: document write use case | `DocumentIndexingService` | Coordinates PDF parsing, chunking, embedding, and persistence. |
| Service: question-answer use case | `RagEngine` | Coordinates retrieval, prompt construction, and answer generation. |
| Domain/business helper | `Retriever` | Implements semantic search and ranking. |
| Domain/business helper | `ParagraphChunker` | Divides extracted page text into searchable passages. |
| Domain/business helper | `PromptBuilder` | Converts a question and retrieved passages into an LLM request. |
| Repository interface | `VectorStore` | Describes persistence operations without naming a database. |
| Repository implementation | `SqliteVectorStore` | Implements `VectorStore` through JDBC and SQLite. |
| External-service interface | `EmbeddingModel` | Describes text-to-vector conversion. |
| External-service implementation | `OpenAiCompatibleEmbeddingModel` | Calls `/v1/embeddings` over HTTP. |
| External-service interface | `ChatModel` | Describes prompt-to-text generation. |
| External-service implementation | `OpenAiCompatibleChatModel` | Calls `/v1/chat/completions` over HTTP. |
| DTO/domain model | records in `rag-model` | Carry validated data between layers. |
| Spring bean wiring | `RagConfiguration` | Selects the concrete repository, clients, and business components. |

The unfamiliar terms map to ordinary backend concepts:

```text
embedding model = an external API client that converts text to numbers
chat model      = an external API client that converts a prompt to an answer
vector store    = a repository that stores text plus its numeric representation
retrieval       = a repository-backed search use case
RAG             = search first, then pass the search results to the chat model
```

## 2. Request flow: upload and index a PDF

```text
POST /api/documents
        |
        v
RagController.upload(...)                         Controller
        |
        v
DocumentIndexingService.index(...)                Service/use case
        |
        +--> PdfDocumentExtractor.extract(...)    Infrastructure adapter
        |
        +--> ParagraphChunker.chunk(...)           Business rule
        |
        +--> EmbeddingModel.embed(...)             External-service interface
        |         |
        |         +--> OpenAiCompatibleEmbeddingModel
        |              calls the configured model server
        |
        +--> VectorStore.save(...)                 Repository interface
                  |
                  +--> SqliteVectorStore
                       commits document, chunks, and vectors
```

Step by step:

1. The controller converts the multipart upload into `filename + byte[]`.
2. The service validates the PDF signature and calculates a SHA-256 checksum.
3. The repository is queried by checksum, just like a uniqueness check in a normal service.
4. PDFBox extracts normal text page by page.
5. The chunker divides each page into smaller passages because search works better on focused passages than entire books.
6. The embedding client sends passage text to a model server. The server returns one `float[]` per passage.
7. The repository saves document metadata, passage text, and vectors in one SQLite transaction.

No LLM writes an answer during indexing. Indexing prepares searchable data.

## 3. Request flow: search without generating an answer

```text
POST /api/search
        |
        v
RagController.search(...)                         Controller
        |
        v
Retriever.search(...)                             Search service/component
        |
        +--> EmbeddingModel.embed(question)        question text -> query vector
        |
        +--> VectorStore.findAllEmbeddedChunks()   load candidate rows
        |
        +--> CosineSimilarity.score(...)           compare query with each row
        |
        +--> filter -> sort -> top K               normal ranking logic
        |
        v
List<SearchResult>                                JSON response
```

The output of vectors is not an answer. Vector comparison produces a ranked list of the stored text passages that are most semantically similar to the question.

## 4. Request flow: ask a question

```text
POST /api/questions
        |
        v
RagController.question(...)                       Controller
        |
        v
RagEngine.answer(...)                             Service/use case
        |
        +--> Retriever.search(...)                 finds relevant passages
        |
        +--> PromptBuilder.build(...)              creates normal text prompt
        |
        +--> ChatModel.generate(...)               external-service interface
                  |
                  +--> OpenAiCompatibleChatModel
                       sends text to the LLM
        |
        v
RagAnswer                                         answer + sources
```

At the application boundary, the LLM receives text. It receives a system instruction, the user's question, and the retrieved passages. It does not receive the stored document vectors. Vectors are used by `Retriever` to decide which original text should be placed in the prompt.

## 5. Embedding model versus LLM

| Concern | Embedding model | Chat LLM |
|---|---|---|
| Backend analogy | Search-index client | Text-generation client |
| Input | One or more text strings | System instruction plus user prompt |
| Output | Fixed-length numeric arrays | Variable-length natural-language text |
| Called while indexing? | Yes | No |
| Called while asking? | Yes, for the question | Yes, after retrieval |
| Purpose | Make semantic similarity measurable | Compose the final grounded answer |
| Database compatibility | Changing it requires re-indexing | Changing it does not require re-indexing |

## 6. Where business logic lives

`RagController` deliberately contains almost no business logic. The important decisions live below it:

- `DocumentIndexingService`: workflow, validation order, deduplication, batching, and the save boundary.
- `ParagraphChunker`: chunk size, overlap, and boundary selection.
- `Retriever`: embedding compatibility, similarity algorithm, threshold, sorting, and result limit.
- `PromptBuilder`: evidence format, citations, and prompt-safety instructions.
- `RagEngine`: ordering of retrieval and generation.
- `SqliteVectorStore`: relational schema and transaction behavior, but not RAG decisions.

This is comparable to keeping controllers thin, services responsible for use cases, and repositories responsible for persistence.

## 7. How V1/V2 replacement works

There are two kinds of change. Choose deliberately.

### A. Improve an implementation without changing its contract

Example: make `CosineSimilarity.score(left, right)` faster while keeping the same inputs and output.

1. Add characterization tests for current behavior.
2. Change the internal algorithm.
3. Keep the public method signature and invariants.
4. Run all tests and benchmark if performance motivated the change.

Callers do not change because the contract is stable.

### B. Keep V1 and V2 available side-by-side

Example: retain character-based chunking while experimenting with semantic chunking.

1. Extract a small interface representing the behavior:

```java
public interface TextChunker {
    List<Chunk> chunk(UUID documentId, String filename, List<DocumentPage> pages);
}
```

2. Rename or retain the current class as one implementation:

```java
public final class ParagraphChunker implements TextChunker { ... }
```

3. Add the alternative:

```java
public final class SemanticChunker implements TextChunker { ... }
```

4. Change `DocumentIndexingService` to depend on `TextChunker`, not a concrete class.
5. Select one implementation in `RagConfiguration` using configuration, `@Qualifier`, or `@Profile`.
6. Run the same contract tests against both implementations.
7. Use a new database path and re-index when the change affects stored chunks or vectors.

This is the same approach as `PaymentServiceV1` and `PaymentServiceV2` implementing a `PaymentService` interface.

## 8. Exact replacement points

| If you want to change... | Stable contract to preserve/create | V1 implementation | Where to select V2 | Must re-index? |
|---|---|---|---|---:|
| Embedding provider | `EmbeddingModel` | `OpenAiCompatibleEmbeddingModel` | `RagConfiguration.embeddingModel` | Yes |
| Chat provider | `ChatModel` | `OpenAiCompatibleChatModel` | `RagConfiguration.chatModel` | No |
| Database | `VectorStore` | `SqliteVectorStore` | `RagConfiguration.vectorStore` | Usually migrate/re-index |
| Chunking strategy | Create `TextChunker` when coexistence is needed | `ParagraphChunker` | `RagConfiguration.paragraphChunker` | Yes |
| Similarity algorithm | Create `SimilarityScorer` when coexistence is needed | `CosineSimilarity` | Inject into `Retriever` | Yes or carefully evaluate |
| Retrieval/ranking | Create `RetrievalService` when coexistence is needed | `Retriever` | `RagConfiguration.retriever` | Depends on algorithm |
| Prompt format | Create `PromptFactory` when coexistence is needed | `PromptBuilder` | `RagConfiguration.promptBuilder` | No |
| PDF parser/OCR | Create `DocumentExtractor` when coexistence is needed | `PdfDocumentExtractor` | `RagConfiguration.pdfDocumentExtractor` | Yes |
| Entire QA workflow | Create `QuestionAnsweringService` when coexistence is needed | `RagEngine` | `RagConfiguration.ragEngine` | No for orchestration-only changes |

## 9. A property-controlled V1/V2 example

The following is an example design, not current production code:

```java
@Bean
TextChunker textChunker(RagProperties properties) {
    return switch (properties.chunking().strategy()) {
        case "paragraph-v1" -> new ParagraphChunker(...);
        case "semantic-v2" -> new SemanticChunker(...);
        default -> throw new IllegalArgumentException("Unknown chunking strategy");
    };
}
```

This provides plug-and-play selection without adding `if (v2)` conditions throughout business logic. Keep selection in `RagConfiguration`; keep algorithms in their own classes.

## 10. Safe refinement checklist

Before replacing any concept:

1. Identify whether the output is persisted. Chunking and embedding changes invalidate indexed data.
2. Write tests that describe the old contract and new intended behavior.
3. Give V2 a separate class while evaluating it; do not scatter version checks inside V1.
4. Select the implementation at Spring configuration level.
5. Keep controllers and API DTOs unchanged unless the HTTP contract genuinely changes.
6. Use a separate SQLite file for experiments so V1 data remains reproducible.
7. Compare retrieval results through `/api/search` before judging final LLM answers.
8. Promote V2 only after measuring retrieval relevance, latency, failures, and resource use.

## 11. Recommended reading order

Read the code in request-flow order rather than Maven-module order:

1. `RagController`
2. `DocumentIndexingService`
3. `RagEngine`
4. `Retriever`
5. `ParagraphChunker`
6. `PromptBuilder`
7. `EmbeddingModel` and `ChatModel`
8. OpenAI-compatible implementations
9. `VectorStore`
10. `SqliteVectorStore`
11. records in `rag-model`
12. `RagConfiguration`

Every important method contains a `Backend flow` explanation and a `How to evolve it` note. Those comments describe the current contract, what callers rely on, and the safest V2 seam.
