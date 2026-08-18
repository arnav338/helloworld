# rag-store-sqlite

## Purpose

Provides zero-server local persistence through SQLite JDBC. The constructor creates the parent directory and idempotent V1 schema. Every operation opens a short-lived connection, enables foreign keys and WAL, and closes resources with try-with-resources.

## Schema

`documents` owns metadata and a unique checksum. `chunks` references a document with cascade deletion. `embeddings` references a chunk and stores model, dimension, and little-endian float32 BLOB.

## Transaction behavior

One `save` transaction inserts a document and all chunks/vectors. Any failure rolls back everything. Deleting a document cascades through both child tables.

## Schema maintenance

The current `CREATE TABLE IF NOT EXISTS` logic is acceptable before release. Before altering a released schema, introduce numbered migrations and record a schema version. Never silently reinterpret an existing vector BLOB.

## Replacing SQLite

Implement `VectorStore` in another module and replace the application bean. Do not copy JDBC assumptions into the API module.

## Study topics

SQLite architecture, JDBC, prepared statements, SQL injection prevention, foreign keys, WAL journaling, ACID transactions, BLOB encoding, IEEE-754, and endianness.

