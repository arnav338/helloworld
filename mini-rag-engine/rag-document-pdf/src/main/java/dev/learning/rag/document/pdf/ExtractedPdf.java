package dev.learning.rag.document.pdf;

import dev.learning.rag.model.DocumentPage;
import java.util.List;

/**
 * Immutable DTO returned by the PDF infrastructure adapter and consumed by the
 * indexing service. It intentionally contains pages, not vectors or chunks.
 *
 * <p>How to evolve it: add provider-neutral extraction metadata here only when
 * every extractor can represent it. Keep PDFBox-specific objects out so a
 * future OCR/Word extractor can share the service boundary.</p>
 */
public record ExtractedPdf(List<DocumentPage> pages) {
    /** Defensively copies the list so parsing results cannot change after construction. */
    public ExtractedPdf {
        pages = List.copyOf(pages);
    }

    /**
     * Reports whether at least one page supplied searchable text. A PDF can be
     * structurally valid yet contain only scanned images.
     */
    public boolean hasExtractableText() {
        return pages.stream().anyMatch(page -> !page.text().isBlank());
    }
}
