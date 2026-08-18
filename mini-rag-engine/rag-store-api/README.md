# rag-store-api

## Purpose

Defines `VectorStore`, the persistence port used by indexing and retrieval. The interface is intentionally small and storage-neutral.

## V1 behavior

V1 stores and retrieves all vectors, then performs exact similarity in Java. This is educational and suitable for a small dataset. A high-scale vector database should eventually expose database-side search rather than returning millions of vectors.

## Adding storage

Create an adapter module, implement atomic save, list/find operations, checksum lookup, vector loading, and cascade deletion. Persist model name and dimension. Test rollback, restart, concurrent access expectations, and deletion.

## Maintenance

Do not leak JDBC, SQLite, pgvector, or vendor types into this interface. Revisit the contract explicitly when approximate nearest-neighbor search becomes a requirement.

## Study topics

Repository pattern, transaction boundaries, exact versus approximate nearest neighbors, HNSW, IVFFlat, ACID, and schema migration.

