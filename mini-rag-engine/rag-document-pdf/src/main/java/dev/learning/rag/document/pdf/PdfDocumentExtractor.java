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
    public ExtractedPdf extract(byte[] bytes) {
        if (bytes == null || bytes.length == 0) throw new IllegalArgumentException("PDF bytes must not be empty");
        try (PDDocument document = Loader.loadPDF(bytes)) {
            if (document.isEncrypted()) throw new IllegalArgumentException("encrypted PDFs are not supported in V1");
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            List<DocumentPage> pages = new ArrayList<>();
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
