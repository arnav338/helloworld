# rag-document-pdf

## Purpose

Adapts Apache PDFBox into a simple `ExtractedPdf` containing one `DocumentPage` per physical page. Page-by-page extraction is deliberate: it preserves reliable citation provenance before chunking.

## Limitations

PDF stores drawing instructions, not semantic paragraphs. Multi-column layout can produce imperfect reading order. Encrypted documents are rejected. Scanned/image-only PDFs return no useful text and require an OCR adapter, which is outside V1.

## Maintenance

Test representative real PDFs before changing PDFBox flags. Keep extraction separate from chunking. If OCR is added, create another adapter or orchestration stage and record whether text came from OCR.

## Plug in another format

Introduce a neutral document-extractor port, implement a new module for Markdown, HTML, DOCX, or OCR, and choose it by media type in the application. Do not make PDFBox a dependency of rag-core.

## Study topics

PDF content streams, text glyph positioning, reading order, OCR, provenance, Apache PDFBox, and document normalization.

