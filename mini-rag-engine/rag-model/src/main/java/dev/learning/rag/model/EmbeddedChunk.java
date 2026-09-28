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
    /**
     * Validates compatibility metadata/numbers and defensively copies mutable
     * array input before the value can cross service/repository boundaries.
     */
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

    /**
     * Returns a copy so callers cannot mutate the record's internal vector.
     * Replace arrays with an immutable vector type only as a coordinated API,
     * codec, provider, and similarity migration.
     */
    @Override
    public float[] vector() {
        return Arrays.copyOf(vector, vector.length);
    }
}
