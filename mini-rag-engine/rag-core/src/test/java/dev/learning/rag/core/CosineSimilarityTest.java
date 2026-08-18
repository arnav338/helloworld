package dev.learning.rag.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CosineSimilarityTest {
    @Test void identicalVectorsScoreOne() { assertEquals(1.0, CosineSimilarity.score(new float[]{1, 2}, new float[]{1, 2}), 1e-12); }
    @Test void orthogonalVectorsScoreZero() { assertEquals(0.0, CosineSimilarity.score(new float[]{1, 0}, new float[]{0, 1}), 1e-12); }
    @Test void rejectsDimensionMismatch() { assertThrows(IllegalArgumentException.class, () -> CosineSimilarity.score(new float[]{1}, new float[]{1, 2})); }
    @Test void rejectsZeroVector() { assertThrows(IllegalArgumentException.class, () -> CosineSimilarity.score(new float[]{0, 0}, new float[]{1, 2})); }
}

