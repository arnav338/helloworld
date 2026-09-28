package dev.learning.rag.document.pdf;

import dev.learning.rag.model.DocumentPage;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Apache PDFBox adapter that preserves page boundaries during text extraction.
 *
 * <p>PDF is a drawing format, not a semantic text format. Reading each page
 * separately provides reliable citation provenance, but columns and unusual
 * layouts can still produce imperfect reading order. Scanned image-only PDFs
 * require OCR and are deliberately rejected by the application in V1.</p>
 *
 * <p>Study topics: PDF content streams, glyph extraction, reading order, OCR,
 * Apache PDFBox {@code PDFTextStripper}.</p>
 */
public final class PdfDocumentExtractor {
    /**
     * Converts uploaded PDF bytes into one provider-neutral DTO per physical
     * page. In backend terms, this is an infrastructure adapter translating a
     * third-party library model into this application's domain model.
     *
     * <p>How to evolve it: PDFBox reading-order improvements can remain here.
     * To support OCR or other document formats side-by-side, introduce a
     * {@code DocumentExtractor} interface, create separate implementations, and
     * select the proper implementation in the service/configuration. Any
     * extraction change requires re-indexing affected documents.</p>
     */
    public ExtractedPdf extract(byte[] bytes) {
        if (bytes == null || bytes.length == 0) throw new IllegalArgumentException("PDF bytes must not be empty");
        try (PDDocument document = Loader.loadPDF(bytes)) {
            if (document.isEncrypted()) throw new IllegalArgumentException("encrypted PDFs are not supported in V1");
            // Position sorting usually makes text follow visual page order more
            // closely; complicated columns can still require a V2 extractor.
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            List<DocumentPage> pages = new ArrayList<>();
            // Reading one page at a time preserves exact page provenance for
            // citations. Reading the full document at once would lose it.
            for (int page = 1; page <= document.getNumberOfPages(); page++) {
                stripper.setStartPage(page);
                stripper.setEndPage(page);
                pages.add(new DocumentPage(page, stripper.getText(document)));
            }
            return new ExtractedPdf(pages);
        } catch (IOException exception) {
            throw new IllegalArgumentException("could not parse PDF", exception);
        }
    }
}
