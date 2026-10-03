# rag-application

## Purpose

This is the executable Spring Boot composition root. It wires interfaces to concrete adapters, validates configuration, exposes REST endpoints, coordinates indexing, and converts expected failures to JSON.

## Main components

- `MiniRagApplication`: process entry point.
- `RagProperties`: type-safe external settings.
- `RagConfiguration`: the only place concrete plug-ins are selected.
- `DocumentIndexingService`: checksum, extraction, chunking, batching, and atomic save.
- `RagController`: upload, list, delete, search, and question endpoints.
- `ApiExceptionHandler`: stable 400/502 error responses.

## Plug-in changes

Add the adapter module to this module's `pom.xml`, then change the relevant bean in `RagConfiguration`. Keep controllers and services dependent on interfaces. Configuration-only model changes belong in environment variables, not Java.

## Maintenance

Controllers should remain thin. Do not place cosine math, SQL, provider JSON, or PDF parsing here. Add request validation at the boundary. Avoid returning stack traces or secrets. If indexing becomes asynchronous, introduce explicit job state rather than hiding background work.

## Running

From the project root, run `./mvnw package` on macOS/Linux or `.\mvnw.cmd package` on Windows. Then run `java -jar rag-application/target/rag-application-0.1.0-SNAPSHOT.jar` (use backslashes or forward slashes on Windows). See the root [operating-system guide](../RUNNING-THE-PROJECT.md) for prerequisites, models, environment variables, API examples, and troubleshooting.

## Study topics

Spring Boot auto-configuration, dependency injection, configuration properties, REST validation, multipart uploads, exception mapping, composition roots, and application services.
