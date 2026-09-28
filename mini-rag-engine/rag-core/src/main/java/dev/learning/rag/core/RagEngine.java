package dev.learning.rag.core;

import dev.learning.rag.model.ChatResponse;
import dev.learning.rag.model.RagAnswer;
import dev.learning.rag.model.SearchResult;
import dev.learning.rag.provider.ChatModel;

import java.util.List;

/**
 * Service-layer implementation of the "answer a document question" use case.
 * This is the closest equivalent to a conventional Spring business service.
 * It coordinates smaller components but contains no MVC, provider HTTP, or SQL.
 *
 * <p>How to evolve it: keep orchestration readable. Add cross-cutting policy
 * (for example, no model call when sources are empty) here. Put new ranking,
 * prompt, or provider algorithms in their own strategies. For side-by-side
 * workflows, extract a {@code QuestionAnsweringService} interface and wire a
 * V1 or V2 implementation in {@code RagConfiguration}.</p>
 */
public final class RagEngine {
    private final Retriever retriever;
    private final PromptBuilder promptBuilder;
    private final ChatModel chatModel;

    /** Constructor injection makes the three sequential use-case stages explicit. */
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
        // Stage 1: search returns original text passages, scores, and citation metadata.
        List<SearchResult> sources = retriever.search(question, topK, minimumScore);

        // Stage 2 builds a text prompt. Stage 3 asks the chat client to generate
        // natural language from that question-plus-evidence text.
        ChatResponse response = chatModel.generate(promptBuilder.build(question, sources));

        // Return answer and evidence together so API clients can verify grounding.
        return new RagAnswer(response.content(), response.model(), sources);
    }
}
