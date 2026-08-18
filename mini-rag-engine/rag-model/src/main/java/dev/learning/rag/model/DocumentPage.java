package dev.learning.rag.model;

/**
 * Text extracted from a single page. Keeping page boundaries before chunking
 * makes source citations deterministic and avoids guessing page numbers later.
 *
 * <p>Study topics: provenance metadata and document-layout extraction.</p>
 */
public record DocumentPage(int pageNumber, String text) {
    public DocumentPage {
        if (pageNumber < 1) throw new IllegalArgumentException("pageNumber starts at 1");
        text = text == null ? "" : text;
    }
}

