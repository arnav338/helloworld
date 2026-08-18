package dev.learning.rag.provider;

import java.util.List;

/**
 * Plug-in boundary for semantic vector generation.
 *
 * <p>To add a provider: implement this interface, translate {@code inputs} to
 * the provider request, validate that one vector returns per input, then select
 * the implementation in application configuration. Do not add provider HTTP
 * details to {@code rag-core}; that would remove replaceability.</p>
 *
 * <p>Study topics: embeddings, ports-and-adapters architecture, dependency
 * inversion, batch APIs, and vector-space compatibility.</p>
 */
public interface EmbeddingModel {
    /**
     * Embeds inputs in their original order. Implementations must return the
     * same number of vectors as inputs and one consistent dimension.
     */
    List<float[]> embed(List<String> inputs);

    /** Stable configured model identifier persisted beside every vector. */
    String modelName();
}

