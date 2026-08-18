package dev.learning.rag.model;

/** Minimal provider-neutral chat result. */
public record ChatResponse(String content, String model) {
    public ChatResponse {
        if (content == null || content.isBlank()) throw new IllegalArgumentException("content is required");
        model = model == null ? "unknown" : model;
    }
}

