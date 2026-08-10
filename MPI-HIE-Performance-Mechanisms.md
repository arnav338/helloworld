# MPI HIE Engine - Performance Mechanisms

These are evidenced by the deployment configuration and inspected Java bytecode.
They describe the platform's throughput and response-time design; they are not
substitutes for measured production latency or capacity benchmarks.

| Mechanism | Role in fast response | How it works in this runtime | Study next |
| --- | --- | --- | --- |
| In-memory patient indexes | Reduces lookup work before matching. | `IndexedPatientStorage` maintains maps keyed by patient ID, local PID, and HIE ID, plus configured field indexes. | HashMap complexity, inverted indexes, composite indexes, cache invalidation, memory sizing |
| Concurrent fan-out / fan-in across MPI nodes | Keeps multi-node search latency close to the slowest node response instead of adding each node call sequentially. | `Mpi2NodeAggregator.search(...)` submits the same `Mpi2SearchAssignment` to **every configured logical node** using virtual threads, then merges `Mpi2NodeSearchResult` objects. It is not request-time patient-to-shard routing. | Fan-out/fan-in, structured concurrency, Java virtual threads, tail latency, distributed result merging, timeout budgets |
| Bulk-operation executor | Increases write throughput under concurrent load. | `BulkOperationExecutor` uses a `ThreadPoolExecutor`; the profile enables a 1 ms buffer window and batches up to 100 operations. | Java ExecutorService, ThreadPoolExecutor sizing, work queues, backpressure, Little's Law |
| Buffered JDBC batch writes | Reduces database round trips and per-row overhead. | The persistence code reuses `PreparedStatement` objects and calls JDBC `addBatch()` for patient writes. | JDBC batching, prepared statements, transaction boundaries, batch-size tuning, write amplification |
| Connection and statement reuse | Avoids repeatedly opening connections and reparsing SQL. | Hibernate/C3P0 pooling is configured with a main maximum of 300 connections; Oracle-specific statement caching is enabled when available. | HikariCP vs C3P0, connection-pool sizing, Oracle statement cache, database session limits |
| Fetch-size tuning and streaming reads | Controls memory and network behavior for large result sets. | Persistence loaders set `PreparedStatement` fetch size and use targeted loader/filter classes. | JDBC fetch size, cursor behavior, pagination, streaming result sets, query plans |
| Asynchronous loaders and audit queries | Keeps non-critical reporting/loading work off the critical path. | The JAR contains asynchronous patient loaders and async audit-search methods; deployment configuration schedules HA and maintenance work. | CompletableFuture, executors, async I/O vs async processing, idempotency, retry patterns |
| Event-driven downstream actions | Keeps forwarding, notifications, logging, and maintenance from blocking identity decisions. | Configured MPI events invoke notification, forwarding, audit, registration, and logging handlers after identity processing. | Domain events, outbox pattern, queues, eventual consistency, failure isolation, observability |

## Important clarification: reactive programming

The inspected core MPI path is **not evidenced as reactive-streams/WebFlux
programming**. Its main persistence path is blocking JDBC/Hibernate. Reactive
programming can improve concurrency and resource utilization for I/O-bound
workloads, but it does not automatically make an application or database query
faster. In this platform, the direct performance mechanisms are indexing,
concurrent fan-out/fan-in, bounded concurrency, batching, pooling, and event
decoupling.

## Important clarification: node selection and HA

The inspected search implementation does **not** identify one relevant MPI node
from a patient ID, MRN, or demographic field. The master calls all configured
logical nodes concurrently. Patient data is partitioned during loading (the
engine exposes modulo/remainder node setup and a `ModuloLoadFilter`), but that
is separate from the live search-dispatch rule.

Within a logical node, HA can select one of its replica endpoints when
`randomizeSearchBetweenNodesInHa` is enabled; the deployment template defaults
that setting to `no`. This is replica availability/load distribution, not
patient-shard selection.
