package dev.learning.rag.document.pdf;

import dev.learning.rag.model.DocumentPage;
import java.util.List;

/** Immutable result of PDF parsing before domain chunks are created. */
public record ExtractedPdf(List<DocumentPage> pages) {
    public ExtractedPdf { pages = List.copyOf(pages); }
    public boolean hasExtractableText() { return pages.stream().anyMatch(page -> !page.text().isBlank()); }
}

