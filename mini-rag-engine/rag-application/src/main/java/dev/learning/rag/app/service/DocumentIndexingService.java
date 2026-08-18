package dev.learning.rag.app.service;

import dev.learning.rag.app.config.RagProperties;
import dev.learning.rag.core.ParagraphChunker;
import dev.learning.rag.document.pdf.PdfDocumentExtractor;
import dev.learning.rag.model.*;
import dev.learning.rag.provider.EmbeddingModel;
import dev.learning.rag.store.VectorStore;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;

/** Coordinates the complete offline indexing path before any database commit. */
@Service
public class DocumentIndexingService {
    private final PdfDocumentExtractor extractor;
    private final ParagraphChunker chunker;
    private final EmbeddingModel embeddingModel;
    private final VectorStore store;
    private final int batchSize;

    public DocumentIndexingService(PdfDocumentExtractor extractor, ParagraphChunker chunker,
                                   EmbeddingModel embeddingModel, VectorStore store, RagProperties properties) {
        this.extractor = extractor; this.chunker = chunker; this.embeddingModel = embeddingModel; this.store = store;
        this.batchSize = properties.embeddingBatchSize();
        if (batchSize < 1) throw new IllegalArgumentException("rag.embedding-batch-size must be positive");
    }

    /**
     * Checks PDF signature, deduplicates by SHA-256, extracts pages, chunks text,
     * embeds in bounded batches, then atomically saves everything. Model failure
     * leaves no half-indexed document in SQLite.
     *
     * <p>Study topics: content hashing, idempotency, batch processing,
     * transactional boundaries, two-phase pipeline design.</p>
     */
    public DocumentRecord index(String originalFilename, byte[] bytes) {
        String filename = safeFilename(originalFilename);
        if (bytes == null || bytes.length < 5 || !new String(bytes, 0, 5, StandardCharsets.US_ASCII).equals("%PDF-")) {
            throw new IllegalArgumentException("file does not have a PDF signature");
        }
        String checksum = sha256(bytes);
        Optional<DocumentRecord> existing = store.findByChecksum(checksum);
        if (existing.isPresent()) return existing.get();

        var extracted = extractor.extract(bytes);
        if (!extracted.hasExtractableText()) {
            throw new IllegalArgumentException("PDF contains no extractable text; scanned PDFs require OCR, which is outside V1");
        }
        UUID documentId = UUID.randomUUID();
        DocumentRecord document = new DocumentRecord(documentId, filename, checksum, extracted.pages().size(), Instant.now());
        List<Chunk> chunks = chunker.chunk(documentId, filename, extracted.pages());
        if (chunks.isEmpty()) throw new IllegalArgumentException("PDF did not produce any usable chunks");

        List<EmbeddedChunk> embedded = new ArrayList<>();
        for (int start = 0; start < chunks.size(); start += batchSize) {
            List<Chunk> batch = chunks.subList(start, Math.min(start + batchSize, chunks.size()));
            List<float[]> vectors = embeddingModel.embed(batch.stream().map(Chunk::text).toList());
            if (vectors.size() != batch.size()) throw new IllegalStateException("embedding provider returned the wrong vector count");
            for (int index = 0; index < batch.size(); index++) {
                embedded.add(new EmbeddedChunk(batch.get(index), embeddingModel.modelName(), vectors.get(index)));
            }
        }
        store.save(document, embedded);
        return document;
    }

    private static String safeFilename(String original) {
        if (original == null || original.isBlank()) return "document.pdf";
        // Both slash styles are handled because uploaded names may originate on
        // another operating system. This prevents path traversal in metadata.
        String normalized = original.replace('\\', '/');
        String name = normalized.substring(normalized.lastIndexOf('/') + 1).strip();
        if (!name.toLowerCase(Locale.ROOT).endsWith(".pdf")) throw new IllegalArgumentException("only .pdf files are accepted");
        return name;
    }

    private static String sha256(byte[] bytes) {
        try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes)); }
        catch (Exception impossible) { throw new IllegalStateException("SHA-256 is required by every Java runtime", impossible); }
    }
}

