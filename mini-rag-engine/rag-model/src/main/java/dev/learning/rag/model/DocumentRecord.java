package dev.learning.rag.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable metadata for one source document.
 *
 * <p>The extracted text is intentionally not stored here. A document is the
 * parent identity; its text lives in {@link Chunk} records so retrieval can
 * cite a precise page and passage.</p>
 *
 * <p>Study topics: domain modelling, immutable value objects, Java records,
 * aggregate roots, and content-addressable storage.</p>
 */
public record DocumentRecord(
        UUID id,
        String filename,
        String checksum,
        int pageCount,
        Instant createdAt) {

    public DocumentRecord {
        Objects.requireNonNull(id, "id");
        filename = requireText(filename, "filename");
        checksum = requireText(checksum, "checksum");
        if (pageCount < 1) {
            throw new IllegalArgumentException("pageCount must be at least 1");
        }
        Objects.requireNonNull(createdAt, "createdAt");
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.strip();
    }
}

