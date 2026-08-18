package dev.learning.rag.model;

/** A retrieved passage, its descending rank, and cosine similarity score. */
public record SearchResult(Chunk chunk, double score, int rank) {
    public SearchResult {
        if (chunk == null) throw new IllegalArgumentException("chunk is required");
        if (!Double.isFinite(score) || score < -1.0 || score > 1.0) throw new IllegalArgumentException("cosine score must be finite and between -1 and 1");
        if (rank < 1) throw new IllegalArgumentException("rank starts at 1");
    }
}

