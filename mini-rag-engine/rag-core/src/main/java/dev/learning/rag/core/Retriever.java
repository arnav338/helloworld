package dev.learning.rag.core;

import dev.learning.rag.model.EmbeddedChunk;
import dev.learning.rag.model.SearchResult;
import dev.learning.rag.provider.EmbeddingModel;
import dev.learning.rag.store.VectorStore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Search service/domain component: converts a natural-language question into
 * ranked stored passages.
 *
 * <p>Backend analogy: this performs work that might normally be split between
 * a search service and repository query. V1 deliberately loads rows through
 * {@link VectorStore} and ranks them in Java so the algorithm remains visible.
 * It returns evidence, not an LLM answer.</p>
 *
 * <p>How to evolve it: tune validation/filtering here if the contract remains
 * exact top-K retrieval. For pgvector, ANN, or hybrid keyword search, introduce
 * a retrieval interface and a V2 implementation that delegates ranking to the
 * database/search engine; select V1/V2 in Spring configuration.</p>
 */
public final class Retriever {
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;

    /**
     * Receives external boundaries through constructor injection. The same
     * embedding model must create stored vectors and question vectors.
     */
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
        // Validate at the service boundary so every caller (HTTP or a future
        // scheduled job) receives the same invariant enforcement.
        if (question == null || question.isBlank()) throw new IllegalArgumentException("question is required");
        if (topK < 1) throw new IllegalArgumentException("topK must be positive");
        if (!Double.isFinite(minimumScore) || minimumScore < -1 || minimumScore > 1) {
            throw new IllegalArgumentException("minimumScore must be between -1 and 1");
        }

        // The question becomes exactly one vector. This external model call
        // produces numbers for search; it does not generate an answer.
        List<float[]> vectors = embeddingModel.embed(List.of(question.strip()));
        if (vectors.size() != 1) throw new IllegalStateException("embedding provider returned an unexpected vector count");
        float[] queryVector = vectors.getFirst();

        // V1 exact search compares every compatible stored vector. It is easy
        // to understand and correct for small datasets, but O(number of chunks).
        List<ScoredChunk> scored = new ArrayList<>();
        for (EmbeddedChunk candidate : vectorStore.findAllEmbeddedChunks()) {
            // Different embedding model names identify different coordinate
            // systems; comparing them would produce a meaningless score.
            if (!candidate.embeddingModel().equals(embeddingModel.modelName())) continue;
            double score = CosineSimilarity.score(queryVector, candidate.vector());
            if (score >= minimumScore) scored.add(new ScoredChunk(candidate, score));
        }
        // A UUID tie-breaker makes equal-score output deterministic.
        scored.sort(Comparator.comparingDouble(ScoredChunk::score).reversed()
                .thenComparing(item -> item.chunk().chunk().id()));

        // Convert the private scoring DTO into public domain results and assign
        // one-based ranks that PromptBuilder can use as citation handles.
        List<SearchResult> results = new ArrayList<>();
        for (int index = 0; index < Math.min(topK, scored.size()); index++) {
            ScoredChunk value = scored.get(index);
            results.add(new SearchResult(value.chunk().chunk(), value.score(), index + 1));
        }
        return List.copyOf(results);
    }

    /** Private intermediate DTO used only between scoring and final ranking. */
    private record ScoredChunk(EmbeddedChunk chunk, double score) { }
}
