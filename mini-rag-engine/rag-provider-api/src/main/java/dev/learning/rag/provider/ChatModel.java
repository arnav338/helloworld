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
    ChatResponse generate(ChatRequest request);
}

