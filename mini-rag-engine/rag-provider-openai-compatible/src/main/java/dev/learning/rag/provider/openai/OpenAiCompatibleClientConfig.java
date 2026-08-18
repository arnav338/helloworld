package dev.learning.rag.provider.openai;

import java.net.URI;
import java.time.Duration;

/** Connection settings shared by chat and embedding adapters. */
public record OpenAiCompatibleClientConfig(
        URI baseUrl,
        String apiKey,
        String model,
        Duration timeout) {
    public OpenAiCompatibleClientConfig {
        if (baseUrl == null || !baseUrl.isAbsolute()) throw new IllegalArgumentException("baseUrl must be absolute");
        apiKey = apiKey == null ? "" : apiKey;
        if (model == null || model.isBlank()) throw new IllegalArgumentException("model is required");
        if (timeout == null || timeout.isNegative() || timeout.isZero()) throw new IllegalArgumentException("timeout must be positive");
    }

    /** Resolves an API route safely whether baseUrl has a trailing slash or not. */
    URI endpoint(String route) {
        String base = baseUrl.toString().replaceAll("/+$", "");
        return URI.create(base + "/" + route.replaceFirst("^/+", ""));
    }
}

