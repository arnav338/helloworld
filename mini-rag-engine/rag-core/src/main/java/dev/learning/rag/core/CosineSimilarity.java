package dev.learning.rag.core;

/**
 * Computes the angle-based similarity used by the exact V1 vector search.
 *
 * <p>Cosine similarity is {@code dot(a,b) / (magnitude(a)*magnitude(b))}.
 * Values near 1 point in the same direction, 0 means orthogonal, and -1 means
 * opposite directions. Embedding models often produce normalized vectors, but
 * this method does not assume normalization.</p>
 *
 * <p>Study topics: dot product, Euclidean norm, cosine similarity, floating
 * point accumulation, vector normalization.</p>
 */
public final class CosineSimilarity {
    private CosineSimilarity() { }

    public static double score(float[] left, float[] right) {
        if (left == null || right == null || left.length == 0 || left.length != right.length) {
            throw new IllegalArgumentException("vectors must be non-empty and have equal dimensions");
        }
        double dot = 0.0;
        double leftSquared = 0.0;
        double rightSquared = 0.0;
        for (int index = 0; index < left.length; index++) {
            if (!Float.isFinite(left[index]) || !Float.isFinite(right[index])) {
                throw new IllegalArgumentException("vectors must contain finite numbers");
            }
            dot += (double) left[index] * right[index];
            leftSquared += (double) left[index] * left[index];
            rightSquared += (double) right[index] * right[index];
        }
        if (leftSquared == 0.0 || rightSquared == 0.0) {
            throw new IllegalArgumentException("cosine similarity is undefined for a zero vector");
        }
        // Clamp tiny floating-point overshoots so the model invariant remains
        // the mathematical interval [-1, 1].
        return Math.max(-1.0, Math.min(1.0, dot / Math.sqrt(leftSquared * rightSquared)));
    }
}

