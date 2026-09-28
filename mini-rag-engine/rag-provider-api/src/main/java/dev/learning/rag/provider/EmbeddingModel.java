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
     *
     * <p>Backend analogy: this is a batched external-client call. Input strings
     * correspond positionally to output arrays. The numeric arrays are later
     * persisted/searched; they are not human-facing answers.</p>
     *
     * <p>How to evolve it: provider-specific request options belong in its
     * implementation/configuration. If a new use case needs fundamentally
     * different behavior, add a new focused port instead of leaking an SDK type.</p>
     */
    List<float[]> embed(List<String> inputs);

    /**
     * Stable configured model identifier persisted beside every vector. It is
     * compatibility metadata: Retriever only compares vectors from this name.
     */
    String modelName();
}
