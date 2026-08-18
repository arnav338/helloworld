# rag-model

## Purpose

This innermost module defines immutable vocabulary shared across the project: documents, pages, chunks, embedded chunks, search results, chat requests/responses, and final RAG answers. It has no third-party dependencies and no knowledge of HTTP, SQL, PDFBox, Spring, Ollama, or SQLite.

## Why it is separate

Adapters need a common language without depending on one another. For example, the SQLite adapter persists `EmbeddedChunk`, the retrieval core reads it, and the REST application serializes `SearchResult`. Putting these values here prevents dependency cycles.

## Design rules

- Records validate invariants in compact constructors.
- Collections use `List.copyOf`.
- Float arrays use defensive copies because arrays remain mutable inside records.
- Page numbers are one-based for human citations; chunk indexes are zero-based for stable internal ordering.
- Model names travel with vectors because vector spaces from different models cannot be mixed.

## Maintenance

Adding a field is a cross-module compatibility change. Find every constructor call, database mapping, JSON response, and test before changing a record. Do not add framework annotations unless there is no neutral alternative. Never put behavior here that needs a provider or database.

## Study topics

Java records, value objects, invariants, defensive copying, domain modelling, aggregate identities, provenance metadata, and binary compatibility.

