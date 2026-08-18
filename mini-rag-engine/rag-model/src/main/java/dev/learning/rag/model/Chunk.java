package dev.learning.rag.model;

import java.util.Objects;
import java.util.UUID;

/**
 * A retrieval-sized passage with enough provenance to create a citation.
 *
 * <p>{@code chunkIndex} is stable within a document and is useful when a page
 * creates multiple chunks. The page number remains the user-facing citation.</p>
 *
 * <p>Study topics: RAG chunking, retrieval granularity, chunk overlap,
 * provenance, precision versus recall.</p>
 */
public record Chunk(
        UUID id,
        UUID documentId,
        String filename,
        int pageNumber,
        int chunkIndex,
        String text) {

    public Chunk {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(documentId, "documentId");
        if (filename == null || filename.isBlank()) throw new IllegalArgumentException("filename must not be blank");
        if (pageNumber < 1) throw new IllegalArgumentException("pageNumber starts at 1");
        if (chunkIndex < 0) throw new IllegalArgumentException("chunkIndex must not be negative");
        if (text == null || text.isBlank()) throw new IllegalArgumentException("chunk text must not be blank");
        filename = filename.strip();
        text = text.strip();
    }
}

