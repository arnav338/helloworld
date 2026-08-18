package dev.learning.rag.core;

import dev.learning.rag.model.Chunk;
import dev.learning.rag.model.DocumentPage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Splits page text into retrieval-sized, overlapping passages.
 *
 * <p>This first implementation measures characters, not tokenizer-specific
 * tokens. That makes it deterministic and provider-neutral. It prefers a
 * paragraph boundary, falls back to whitespace, and finally performs a hard
 * split for an unusually long unbroken string.</p>
 *
 * <p>Study topics: sliding windows, text segmentation, chunk overlap,
 * tokenizer-aware chunking, semantic chunking, retrieval precision/recall.</p>
 */
public final class ParagraphChunker {
    private final int maximumCharacters;
    private final int overlapCharacters;

    public ParagraphChunker(int maximumCharacters, int overlapCharacters) {
        if (maximumCharacters < 100) throw new IllegalArgumentException("maximumCharacters must be at least 100");
        if (overlapCharacters < 0 || overlapCharacters >= maximumCharacters) {
            throw new IllegalArgumentException("overlapCharacters must be non-negative and smaller than maximumCharacters");
        }
        this.maximumCharacters = maximumCharacters;
        this.overlapCharacters = overlapCharacters;
    }

    /**
     * Chunks every page independently so a chunk always has one unambiguous
     * citation page. Empty pages are skipped rather than embedded.
     */
    public List<Chunk> chunk(UUID documentId, String filename, List<DocumentPage> pages) {
        if (documentId == null) throw new IllegalArgumentException("documentId is required");
        if (filename == null || filename.isBlank()) throw new IllegalArgumentException("filename is required");
        if (pages == null) throw new IllegalArgumentException("pages are required");

        List<Chunk> result = new ArrayList<>();
        int globalChunkIndex = 0;
        for (DocumentPage page : pages) {
            // Normalize platform line endings but deliberately preserve blank
            // lines because they are useful paragraph-boundary signals.
            String text = page.text().replace("\r\n", "\n").replace('\r', '\n').strip();
            if (text.isBlank()) continue;

            int start = 0;
            while (start < text.length()) {
                int desiredEnd = Math.min(start + maximumCharacters, text.length());
                int end = chooseNaturalBoundary(text, start, desiredEnd);
                String value = text.substring(start, end).strip();
                if (!value.isBlank()) {
                    result.add(new Chunk(UUID.randomUUID(), documentId, filename,
                            page.pageNumber(), globalChunkIndex++, value));
                }
                if (end == text.length()) break;

                // Move backwards to create overlap. Advancing by at least one
                // character guarantees termination even around whitespace.
                int nextStart = Math.max(start + 1, end - overlapCharacters);
                while (nextStart < end && Character.isWhitespace(text.charAt(nextStart))) nextStart++;
                start = nextStart;
            }
        }
        return List.copyOf(result);
    }

    /** Finds the best boundary near the target without creating tiny chunks. */
    private int chooseNaturalBoundary(String text, int start, int desiredEnd) {
        if (desiredEnd == text.length()) return desiredEnd;
        int minimumUsefulEnd = start + maximumCharacters / 2;
        int paragraph = text.lastIndexOf("\n\n", desiredEnd);
        if (paragraph >= minimumUsefulEnd) return paragraph;
        for (int index = desiredEnd; index >= minimumUsefulEnd; index--) {
            if (Character.isWhitespace(text.charAt(index - 1))) return index;
        }
        return desiredEnd;
    }
}

