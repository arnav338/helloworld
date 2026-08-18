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

    public record Model(URI baseUrl, String apiKey, String model, Duration timeout) { }
    public record Store(Path path) { }
    public record Chunking(int maximumCharacters, int overlapCharacters) { }
    public record Retrieval(int topK, double minimumScore) { }
}

