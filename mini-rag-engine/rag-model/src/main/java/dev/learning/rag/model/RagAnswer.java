package dev.learning.rag.model;

import java.util.List;

/** Final API-facing RAG outcome with inspectable retrieval evidence. */
public record RagAnswer(String answer, String chatModel, List<SearchResult> sources) {
    public RagAnswer {
        if (answer == null || answer.isBlank()) throw new IllegalArgumentException("answer is required");
        chatModel = chatModel == null ? "unknown" : chatModel;
        sources = List.copyOf(sources == null ? List.of() : sources);
    }
}
