# rag-core

## Purpose

Contains the concepts the project exists to teach: paragraph-aware overlapping chunks, cosine similarity, exact semantic ranking, evidence prompt construction, and retrieval-plus-generation orchestration. All classes are plain Java and can be tested without Spring, a database server, network, or model runtime.

## Important classes

- `ParagraphChunker`: page-local sliding windows with natural boundary preference.
- `CosineSimilarity`: dot product divided by vector magnitudes, with numerical validation.
- `Retriever`: query embedding, model compatibility, score filtering, sort, top-k, rank.
- `PromptBuilder`: labelled untrusted evidence and a refusal policy.
- `RagEngine`: coordinates search, prompt, and chat generation.

## Changing chunking

First add evaluation examples and unit tests. Preserve page provenance and guarantee loop progress. Compare retrieval hit rate before and after changing size or overlap. Token-aware or semantic chunking should be a new implementation behind a future `Chunker` interface rather than unrelated conditions inside the current class.

## Changing similarity

Add mathematical tests for identical, orthogonal, opposite, zero, non-finite, and mismatched vectors. Confirm the embedding provider's recommended metric. Never compare different model spaces.

## Maintenance

No Spring, HTTP, JDBC, Jackson, PDFBox, or provider SDK imports belong here. Failures should state which invariant failed. Retrieval and generation must remain independently callable.

## Study topics

Sliding windows, semantic segmentation, precision/recall, linear algebra, cosine distance, top-k algorithms, prompt injection, grounded generation, and context engineering.

