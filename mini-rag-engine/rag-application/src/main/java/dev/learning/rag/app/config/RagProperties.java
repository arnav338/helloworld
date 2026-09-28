package dev.learning.rag.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;

/**
 * Type-safe external configuration. Every field can be overridden in YAML,
 * environment variables, or command-line flags without recompiling.
 * Search topics: Spring ConfigurationProperties and twelve-factor configuration.
 */
@ConfigurationProperties(prefix = "rag")
public record RagProperties(
        Model chat,
        Model embedding,
        Store store,
        Chunking chunking,
        Retrieval retrieval,
        int embeddingBatchSize) {

    /** Settings for one external model API client. Chat and embedding use separate instances. */
    public record Model(URI baseUrl, String apiKey, String model, Duration timeout) { }

    /** Repository settings; V1 needs only the local SQLite file path. */
    public record Store(Path path) { }

    /** Business settings controlling passage size and repeated context. */
    public record Chunking(int maximumCharacters, int overlapCharacters) { }

    /** Default search limit and similarity cutoff used when a request omits them. */
    public record Retrieval(int topK, double minimumScore) { }
}
