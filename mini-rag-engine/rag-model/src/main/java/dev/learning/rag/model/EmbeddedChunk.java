package dev.learning.rag.model;

import java.util.Arrays;

/**
 * A chunk paired with the vector produced by a specific embedding model.
 *
 * <p>Arrays are mutable in Java, even inside records. The constructor and
 * accessor therefore copy the vector to preserve this value object's
 * immutability. Search topic: "defensive copying Java arrays".</p>
 */
public record EmbeddedChunk(Chunk chunk, String embeddingModel, float[] vector) {
    public EmbeddedChunk {
        if (chunk == null) throw new IllegalArgumentException("chunk is required");
        if (embeddingModel == null || embeddingModel.isBlank()) throw new IllegalArgumentException("embeddingModel is required");
        if (vector == null || vector.length == 0) throw new IllegalArgumentException("vector must not be empty");
        embeddingModel = embeddingModel.strip();
        vector = Arrays.copyOf(vector, vector.length);
        for (float value : vector) {
            if (!Float.isFinite(value)) throw new IllegalArgumentException("vector contains a non-finite value");
        }
    }

    @Override
    public float[] vector() {
        return Arrays.copyOf(vector, vector.length);
    }
}

