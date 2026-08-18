package dev.learning.rag.core;

import dev.learning.rag.model.EmbeddedChunk;
import dev.learning.rag.model.SearchResult;
import dev.learning.rag.provider.EmbeddingModel;
import dev.learning.rag.store.VectorStore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Performs exact semantic retrieval while keeping every ranking step visible. */
public final class Retriever {
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;

    public Retriever(EmbeddingModel embeddingModel, VectorStore vectorStore) {
        this.embeddingModel = embeddingModel;
        this.vectorStore = vectorStore;
    }

    /**
     * Embeds the question, rejects vectors from another model, scores all
     * compatible chunks, applies a threshold, sorts descending, and assigns
     * one-based ranks. Study topics: brute-force k-nearest-neighbor search,
     * top-k retrieval, score thresholds, stable sorting, embedding drift.
     */
    public List<SearchResult> search(String question, int topK, double minimumScore) {
        if (question == null || question.isBlank()) throw new IllegalArgumentException("question is required");
        if (topK < 1) throw new IllegalArgumentException("topK must be positive");
        if (!Double.isFinite(minimumScore) || minimumScore < -1 || minimumScore > 1) {
            throw new IllegalArgumentException("minimumScore must be between -1 and 1");
        }

        List<float[]> vectors = embeddingModel.embed(List.of(question.strip()));
        if (vectors.size() != 1) throw new IllegalStateException("embedding provider returned an unexpected vector count");
        float[] queryVector = vectors.getFirst();

        List<ScoredChunk> scored = new ArrayList<>();
        for (EmbeddedChunk candidate : vectorStore.findAllEmbeddedChunks()) {
            if (!candidate.embeddingModel().equals(embeddingModel.modelName())) continue;
            double score = CosineSimilarity.score(queryVector, candidate.vector());
            if (score >= minimumScore) scored.add(new ScoredChunk(candidate, score));
        }
        scored.sort(Comparator.comparingDouble(ScoredChunk::score).reversed()
                .thenComparing(item -> item.chunk().chunk().id()));

        List<SearchResult> results = new ArrayList<>();
        for (int index = 0; index < Math.min(topK, scored.size()); index++) {
            ScoredChunk value = scored.get(index);
            results.add(new SearchResult(value.chunk().chunk(), value.score(), index + 1));
        }
        return List.copyOf(results);
    }

    private record ScoredChunk(EmbeddedChunk chunk, double score) { }
}

