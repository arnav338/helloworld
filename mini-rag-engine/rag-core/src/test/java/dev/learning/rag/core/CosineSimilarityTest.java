package dev.learning.rag.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CosineSimilarityTest {
    /** Same direction must produce the maximum cosine score, regardless of use case. */
    @Test
    void identicalVectorsScoreOne() {
        assertEquals(1.0, CosineSimilarity.score(new float[]{1, 2}, new float[]{1, 2}), 1e-12);
    }

    /** Perpendicular vectors have no directional similarity and therefore score zero. */
    @Test
    void orthogonalVectorsScoreZero() {
        assertEquals(0.0, CosineSimilarity.score(new float[]{1, 0}, new float[]{0, 1}), 1e-12);
    }

    /** Different dimensions represent incompatible coordinate systems and must fail fast. */
    @Test
    void rejectsDimensionMismatch() {
        assertThrows(IllegalArgumentException.class,
                () -> CosineSimilarity.score(new float[]{1}, new float[]{1, 2}));
    }

    /** A zero vector has no direction, so cosine similarity is mathematically undefined. */
    @Test
    void rejectsZeroVector() {
        assertThrows(IllegalArgumentException.class,
                () -> CosineSimilarity.score(new float[]{0, 0}, new float[]{1, 2}));
    }
}
