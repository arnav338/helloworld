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
    /** Utility class: callers use {@link #score(float[], float[])}; no object state is required. */
    private CosineSimilarity() { }

    /**
     * Converts two equal-length vectors into one comparable score in [-1, 1].
     * This is analogous to a backend scoring function: it accepts two values
     * and returns a deterministic ranking signal; it does not call a model or
     * database.
     *
     * <p>Algorithm: the dot product measures aligned movement, while each
     * magnitude removes vector-length effects. Dividing them compares direction
     * (semantic orientation) rather than raw numeric size.</p>
     *
     * <p>How to evolve it: optimize this implementation in place only if the
     * mathematical contract stays cosine similarity. To compare cosine against
     * dot product or Euclidean distance, introduce a {@code SimilarityScorer}
     * interface, create named V1/V2 implementations, inject it into
     * {@link Retriever}, and evaluate both on the same question set.</p>
     */
    public static double score(float[] left, float[] right) {
        // Coordinate N on the left must represent the same learned feature as
        // coordinate N on the right, so dimensions must match exactly.
        if (left == null || right == null || left.length == 0 || left.length != right.length) {
            throw new IllegalArgumentException("vectors must be non-empty and have equal dimensions");
        }
        // Accumulate with double precision even though provider data is float;
        // this reduces rounding error across hundreds/thousands of dimensions.
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
