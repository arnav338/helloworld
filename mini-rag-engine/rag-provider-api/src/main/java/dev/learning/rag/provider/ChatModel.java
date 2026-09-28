package dev.learning.rag.provider;

import dev.learning.rag.model.ChatRequest;
import dev.learning.rag.model.ChatResponse;

/**
 * Plug-in boundary for natural-language generation.
 *
 * <p>A new provider adapter needs only to implement this contract. Retrieval,
 * SQLite, PDF processing, and controllers remain unchanged. Unlike changing an
 * embedding model, changing a chat model does not invalidate stored vectors.</p>
 */
public interface ChatModel {
    /**
     * Sends a provider-neutral text-generation request and returns generated
     * text. Implementations translate transport details; callers know no JSON,
     * URL, SDK, or authentication mechanism.
     *
     * <p>How to evolve it: implement this unchanged for another provider. Add a
     * V2 method only when every provider must support a genuinely new capability
     * such as streaming; prefer a separate interface for optional features.</p>
     */
    ChatResponse generate(ChatRequest request);
}
