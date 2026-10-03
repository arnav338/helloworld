# Mini RAG Engine: Use Cases and Detailed Setup Patterns

This guide explains where the project is genuinely useful, how each use case should be assembled, which APIs to call, what to verify, and where V1 stops being sufficient. It assumes no previous RAG experience.

Start with [RUNNING-THE-PROJECT.md](RUNNING-THE-PROJECT.md) to install prerequisites and start the application on macOS, Linux, or Windows.

Most compact configuration examples below use macOS/Linux shell syntax. On Windows PowerShell, set the same value first—for example, `$env:RAG_DATABASE_PATH = '.\data\support-runbooks.db'`—and then run `.\start-local.cmd`. The operating-system guide contains complete command equivalents.

## 1. What this project provides

The Mini RAG Engine turns a collection of text-based PDFs into a searchable, question-answerable local knowledge base.

It has two related capabilities:

1. **Semantic search** returns passages whose meaning is related to a question, even when the exact words differ.
2. **Grounded question answering** gives the retrieved passages to a chat model and returns an answer together with its sources.

The project is especially useful when all of these are true:

- The knowledge is contained in a manageable collection of text-based PDFs.
- Users need meaning-based search or cited answers rather than filename search.
- A local, inspectable prototype is preferable to cloud infrastructure.
- A Java/Spring team wants to understand and control the RAG pipeline.
- Exact vector scanning in one local SQLite database is sufficient.

It is not yet a production knowledge platform. V1 does not include a UI, OCR, authentication, user permissions, multi-tenancy, distributed storage, asynchronous indexing, document synchronization, approximate-nearest-neighbor search, or operational deployment manifests.

## 2. Choose a use case

| Use case | Primary endpoint | Typical documents | Main value |
|---|---|---|---|
| Personal document assistant | `/api/questions` | Manuals, policies, reference PDFs | Ask natural-language questions with citations |
| Support/runbook assistant | `/api/search` then `/api/questions` | Runbooks, troubleshooting guides, error catalogs | Find recovery procedures from symptoms |
| Developer documentation search | `/api/search` | Architecture, API, onboarding, ADR PDFs | Find conceptually related technical passages |
| Compliance/evidence explorer | `/api/search` | Policies, controls, audit guidance | Locate and cite evidence without relying only on generated prose |
| Study/research companion | Both | Papers, course notes, books with extractable text | Review concepts and trace answers to pages |
| Semantic-search backend | `/api/search` only | Any curated text-based PDFs | Add retrieval to another application without an LLM answer |
| RAG engineering laboratory | Both | Small controlled test corpus | Learn, measure, and replace individual RAG components |
| Local privacy-oriented prototype | Both | Internal but non-regulated prototype documents | Keep model inference and storage on one workstation |

## 3. Common foundation for every use case

Every scenario begins with the same five layers:

```text
Client
  -> Spring REST controller
     -> indexing/retrieval services
        -> SQLite document and vector store
        -> Ollama embedding model
        -> Ollama chat model (only for /api/questions)
```

### 3.1 Start and validate the platform

Use the launcher documented in [RUNNING-THE-PROJECT.md](RUNNING-THE-PROJECT.md):

```bash
# macOS/Linux
./start-local.sh
```

```powershell
# Windows
.\start-local.cmd
```

Confirm:

- `http://localhost:8080/actuator/health` reports `UP`.
- `ollama list` contains the configured embedding and chat models.
- The startup script reports that both real model API checks passed.

### 3.2 Curate documents before indexing

Good retrieval begins with good source material. Use PDFs that:

- Contain selectable text rather than only scanned page images.
- Have meaningful headings, paragraphs, and page boundaries.
- Represent the current approved version of the knowledge.
- Avoid duplicated or contradictory versions unless version comparison is intentional.
- Do not contain information that the local machine/user is not authorized to store.

V1 calculates a SHA-256 checksum and avoids indexing identical bytes twice. A revised PDF is a different document; delete the obsolete document explicitly if both versions should not remain searchable.

### 3.3 Index each document

```bash
curl --fail -F 'file=@/absolute/path/to/document.pdf' \
  http://localhost:8080/api/documents
```

List the indexed collection:

```bash
curl --fail http://localhost:8080/api/documents
```

Indexing performs PDF extraction, paragraph-aware chunking, embedding calls, vector validation, and one transactional SQLite save. A failure before the save does not create a partial document.

### 3.4 Establish a retrieval baseline

Before using generated answers, ask several questions through `/api/search`:

```bash
curl --fail -X POST http://localhost:8080/api/search \
  -H 'Content-Type: application/json' \
  -d '{"question":"How is access revoked?","topK":5,"minimumScore":0.20}'
```

Check whether the expected filename, page, and passage occur near rank 1. If retrieval is wrong, the answer model never receives the right evidence. Fix document selection, embedding consistency, chunking, `topK`, or `minimumScore` before changing prompts.

### 3.5 Test grounded answering

```bash
curl --fail -X POST http://localhost:8080/api/questions \
  -H 'Content-Type: application/json' \
  -d '{"question":"How is access revoked?"}'
```

Review both `answer` and `sources`. A plausible answer without a relevant source should not be treated as verified knowledge.

## 4. Use case: personal document assistant

### Goal

Ask questions across product manuals, benefits guides, household reference documents, or personal learning material without opening and searching each PDF separately.

### Recommended setup

| Area | Choice |
|---|---|
| Host | Personal macOS, Linux, or Windows machine |
| Model runtime | Local Ollama |
| Database | Default `./data/rag.db` |
| Corpus | One topic or closely related set of PDFs |
| Interface | `curl`, Postman, or a small future UI |
| Retrieval starting point | `topK=5`, `minimumScore=0.20` to `0.25` |

### Setup sequence

1. Start the application with the OS launcher.
2. Create one separate database per unrelated collection to prevent noisy cross-topic matches.
3. Upload a few representative PDFs, not an uncontrolled folder dump.
4. Ask factual questions with answers that visibly exist in the documents.
5. Compare returned sources with the original pages.
6. Add more documents only after the initial retrieval baseline works.

Example collection isolation:

```bash
RAG_DATABASE_PATH=./data/appliance-manuals.db ./start-local.sh
```

### Useful questions

- “What maintenance is required every six months?”
- “What does error code E17 mean?”
- “Which exclusions apply to this benefit?”

### Important limits

The application has no login or per-document permissions. Anyone who can reach the API can query or delete the local collection. Do not expose it beyond the trusted workstation without adding security controls.

## 5. Use case: support and runbook assistant

### Goal

Turn operational runbooks, known-error documents, deployment guides, and recovery procedures into a symptom-oriented search and cited troubleshooting assistant.

### Why semantic retrieval helps

A log might say “connection refused,” while the runbook says “control-plane endpoint unavailable.” Exact keyword search can miss that relationship; embeddings may place the meanings close enough for retrieval.

### Recommended setup

| Area | Choice |
|---|---|
| Corpus | Approved runbooks, error catalogs, deployment and rollback procedures |
| Database | Environment/team-specific file, such as `data/payments-support.db` |
| First API | `/api/search` so engineers see raw procedures and scores |
| Second API | `/api/questions` for a concise evidence-grounded explanation |
| Retrieval | Begin with a larger `topK` such as 8; tune using real incident questions |
| Governance | Record document version/date in the PDF filename until richer metadata exists |

### Detailed workflow

1. Remove obsolete runbooks or clearly mark their version in filenames.
2. Index recovery documentation, not raw live logs. V1 accepts PDFs only.
3. Build a list of 20–50 historical symptoms and their expected document/page.
4. Call `/api/search` for each question and record whether the expected passage appears in the top results.
5. Adjust `RAG_TOP_K` and `RAG_MINIMUM_SCORE` using those results.
6. Ask `/api/questions` only after retrieval quality is acceptable.
7. During an incident, treat the output as decision support; verify destructive or production-changing steps against the cited runbook.

Example configuration:

```bash
RAG_DATABASE_PATH=./data/support-runbooks.db \
RAG_TOP_K=8 \
RAG_MINIMUM_SCORE=0.18 \
./start-local.sh
```

### Production evolution

Before shared operational use, add authentication, document ownership, audit logging, a document approval/version process, rate limits, TLS, observability, and a human confirmation boundary for any operational action. The current project never executes retrieved instructions.

## 6. Use case: developer documentation and onboarding search

### Goal

Help developers navigate architecture documents, API guides, onboarding manuals, and architectural decision records exported as PDFs.

### Recommended setup

- Use one database per product or bounded platform domain.
- Prefer documents with descriptive headings and examples.
- Include architecture and operations material only when cross-domain retrieval is wanted.
- Use `/api/search` in developer tooling because it returns raw passages and metadata that another UI can render.

### Detailed workflow

1. Export stable documentation snapshots to text-based PDFs.
2. Index them and retain meaningful filenames such as `payments-api-v3.pdf`.
3. Test vocabulary mismatches: ask using developer language that differs from formal architecture terminology.
4. Display filename, page, score, and chunk text in the consuming UI.
5. Link users back to the authoritative document; the local database is an index, not the source-of-truth editor.

Example searches:

- “Where is request idempotency enforced?”
- “Which component owns retry policy?”
- “How do I run the service locally?”

### When to extend the project

If source documents change daily, manual PDF upload becomes the bottleneck. Add a connector/synchronization job and explicit update semantics rather than repeatedly scripting the upload endpoint without document lifecycle tracking.

## 7. Use case: compliance and evidence exploration

### Goal

Locate relevant passages in policies, control descriptions, audit guidance, and evidence packs while retaining page-level provenance.

### Recommended operating mode

Use `/api/search` as the authoritative feature. Generated summaries can help a reviewer understand results, but should not replace the source passage or professional judgment.

### Detailed setup

1. Create a dedicated database for a defined audit period or control family.
2. Index only approved, dated document versions.
3. Use filenames that contain policy identifier and effective date.
4. Create test questions mapped to known control evidence.
5. Inspect the retrieved passage and original page for every conclusion.
6. Back up the SQLite file while the application is stopped if the indexed snapshot must be retained.

Example:

```bash
RAG_DATABASE_PATH=./data/audit-2026-q4.db \
RAG_TOP_K=10 \
RAG_MINIMUM_SCORE=0.15 \
./start-local.sh
```

### Limits and cautions

- A similarity score is not proof that a control is satisfied.
- The model can summarize evidence incorrectly.
- V1 does not provide immutable audit logs, access controls, retention enforcement, encryption-key management, or legal hold.
- Sensitive/regulatory data requires an approved environment and controls beyond “runs locally.”

## 8. Use case: study and research companion

### Goal

Explore papers, course notes, and books by concept; generate explanations while retaining the passages that informed them.

### Recommended setup

| Choice | Recommendation |
|---|---|
| Corpus | One course, research topic, or reading list per database |
| Search | Use broad conceptual questions and inspect top 5–10 passages |
| Answering | Ask comparison/explanation questions only after relevant sources appear |
| Verification | Read cited pages; do not cite the generated answer as the original author |

### Example workflow

1. Index lecture notes and text-based papers.
2. Search “How do the authors define vector similarity?”
3. Inspect passages and scores.
4. Ask “Compare the definitions using only the supplied evidence.”
5. Open the cited pages before using the conclusion in formal work.

Scanned research papers need OCR before upload. Complex tables, diagrams, equations, footnotes, and multi-column layouts may not extract cleanly with V1 PDFBox text extraction.

## 9. Use case: semantic-search service without generated answers

### Goal

Use this project as a local retrieval backend for another Java service, desktop tool, or prototype UI without allowing an LLM to formulate the final answer.

### Why this is useful

- Lower latency and compute than a chat completion.
- Every result is directly inspectable.
- The consuming application keeps control of presentation and decisions.
- Only the embedding capability is used per query.

### Setup

1. Start the full application normally; the current launcher validates both models.
2. Index the collection.
3. Integrate only `POST /api/search` and `GET /api/documents`.
4. Render result text, filename, page number, rank, and score.
5. Do not call `/api/questions`.

For a true embedding-only deployment, evolve application wiring/startup validation so the chat adapter is optional. V1 configuration creates both clients, even though `/api/search` itself calls only the embedding model.

### Integration contract

Request:

```json
{
  "question": "How are expired credentials refreshed?",
  "topK": 5,
  "minimumScore": 0.2
}
```

Response elements include `chunk.filename`, `chunk.pageNumber`, `chunk.text`, `score`, and `rank`. Keep these provenance fields visible instead of returning anonymous snippets.

## 10. Use case: RAG engineering and learning laboratory

### Goal

Understand RAG through normal backend layers and replace one implementation at a time without hiding behavior inside a large AI framework.

### Experiments supported by the architecture

| Experiment | Change point | What remains stable |
|---|---|---|
| New chat model | Environment variables or `ChatModel` adapter | Retrieval and stored embeddings |
| New embedding model | Configuration/`EmbeddingModel` adapter plus re-index | Controllers and RAG orchestration |
| Token/semantic chunking | Introduce/replace a chunker service | Provider and storage contracts |
| PostgreSQL/pgvector | New `VectorStore` implementation | Controller and core use cases |
| Native provider API | New provider adapter module | Core domain and REST API |
| Retrieval tuning | `topK`, threshold, ranking implementation | Indexing and chat adapter |

### Suggested learning sequence

1. Trace PDF upload from `RagController` to `DocumentIndexingService` and SQLite.
2. Inspect stored chunks and understand why page provenance is retained.
3. Call `/api/search` and trace question embedding, cosine similarity, ranking, and thresholding.
4. Call `/api/questions` and inspect how `PromptBuilder` supplies retrieved evidence.
5. Replace only the chat model and confirm re-indexing is unnecessary.
6. Replace the embedding model, use a new database, and compare retrieval.
7. Add an evaluation set before changing chunk size or ranking logic.

Read [BACKEND-DEVELOPER-GUIDE.md](BACKEND-DEVELOPER-GUIDE.md) for the Controller → Service → Repository mapping.

## 11. Use case: local privacy-oriented prototype

### Goal

Demonstrate document Q&A without sending prompts/documents to a hosted model provider.

### Setup

- Run Spring Boot, SQLite, and Ollama on the same trusted workstation.
- Leave both model base URLs on `127.0.0.1`/`localhost`.
- Do not configure cloud API keys.
- Restrict access to the application port with local firewall/network policy.
- Store the project and `data/rag.db` on approved encrypted storage where required.
- Inspect backup and endpoint-security behavior because local files can still be copied by privileged software/users.

### What “local” does and does not guarantee

Local Ollama prevents the application from intentionally sending model requests to a hosted provider. It does not automatically provide authentication, disk encryption, malware protection, data classification approval, secure backups, OS hardening, or compliance certification. Those controls belong to the environment.

## 12. Designing separate knowledge bases

The simplest V1 isolation mechanism is a different SQLite file per collection:

```text
data/
  product-manuals.db
  support-runbooks.db
  developer-onboarding.db
  study-course-a.db
```

Start one selected collection by changing `RAG_DATABASE_PATH`. Do not point multiple simultaneously running application processes at the same writable SQLite file for shared production traffic.

Separate databases are useful when:

- Collections have unrelated vocabulary.
- Different people should eventually have different permissions.
- The embedding model or retrieval settings are being compared.
- A time-bounded evidence snapshot must be retained.
- Test data should never mix with real documents.

## 13. Retrieval tuning method

Do not tune from one impressive demo question. Create an evaluation table:

| Question | Expected document/page | Expected in top-k? | Observed rank | Notes |
|---|---|---:|---:|---|
| How are retries handled? | operations.pdf p.12 | Yes | 2 | Correct but second |
| Who approves access? | policy.pdf p.8 | Yes | — | Missed |

Then vary one factor at a time:

- `RAG_TOP_K`: more passages improve recall but add noise and prompt size.
- `RAG_MINIMUM_SCORE`: lower values admit weaker matches; higher values can remove useful evidence.
- Chunk maximum/overlap: affects passage focus and context continuity; requires re-indexing.
- Embedding model: changes the vector space and requires a fresh database/re-index.
- Document quality: often matters more than prompt wording.

Measure retrieval first. A chat model cannot recover evidence that retrieval omitted.

## 14. From local prototype to shared service

Before calling the project production-ready, plan explicit work in these areas:

| Area | Needed evolution |
|---|---|
| Security | Authentication, authorization, TLS, secrets management, input limits, audit logging |
| Data lifecycle | Update/version semantics, retention, backups, encryption, deletion verification |
| Ingestion | Async jobs, status, retries, OCR, additional formats, connectors |
| Scale | Database server, vector index, pagination, caching, concurrency/load tests |
| Quality | Retrieval evaluation set, regression metrics, groundedness checks, model/version tracking |
| Operations | Deployment packaging, metrics, tracing, alerts, health/readiness, resource limits |
| UX | Upload/search/question interface, source links, feedback, admin controls |
| Governance | Approved sources, ownership, access policy, review cadence, incident handling |

The current modular boundaries are intended to make those changes possible, but the features themselves are not silently present.

## 15. First-time-user checklist

- [ ] Read [RUNNING-THE-PROJECT.md](RUNNING-THE-PROJECT.md) and start the correct OS launcher.
- [ ] Confirm Spring health is `UP`.
- [ ] Confirm the two Ollama model API tests pass.
- [ ] Choose one narrow use case and one dedicated database.
- [ ] Select a few authoritative, text-based PDFs.
- [ ] Upload and list documents.
- [ ] Test `/api/search` with known-answer questions.
- [ ] Verify filename, page, passage, rank, and score.
- [ ] Test `/api/questions` and inspect its returned sources.
- [ ] Record weak/missed questions instead of tuning from memory.
- [ ] Keep the API local until authentication/TLS and governance are added.
- [ ] Use a new database and re-index whenever the embedding model changes.
