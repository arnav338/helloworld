package dev.learning.rag.core;

import dev.learning.rag.model.ChatResponse;
import dev.learning.rag.model.RagAnswer;
import dev.learning.rag.model.SearchResult;
import dev.learning.rag.provider.ChatModel;

import java.util.List;

/** Coordinates retrieval and generation; it contains no HTTP or SQL code. */
public final class RagEngine {
    private final Retriever retriever;
    private final PromptBuilder promptBuilder;
    private final ChatModel chatModel;

    public RagEngine(Retriever retriever, PromptBuilder promptBuilder, ChatModel chatModel) {
        this.retriever = retriever;
        this.promptBuilder = promptBuilder;
        this.chatModel = chatModel;
    }

    /**
     * The orchestration is intentionally short: retrieve, build controlled
     * context, generate, return evidence. Keeping it short makes failure
     * attribution clear: search can be tested independently from generation.
     */
    public RagAnswer answer(String question, int topK, double minimumScore) {
        List<SearchResult> sources = retriever.search(question, topK, minimumScore);
        ChatResponse response = chatModel.generate(promptBuilder.build(question, sources));
        return new RagAnswer(response.content(), response.model(), sources);
    }
}

