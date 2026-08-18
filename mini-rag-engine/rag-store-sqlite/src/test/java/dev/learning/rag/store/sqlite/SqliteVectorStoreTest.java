package dev.learning.rag.store.sqlite;

import dev.learning.rag.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SqliteVectorStoreTest {
    @TempDir Path temporaryDirectory;

    @Test void persistsAndCascadeDeletesDocumentWithVector() {
        var store = new SqliteVectorStore(temporaryDirectory.resolve("rag.db"));
        UUID documentId = UUID.randomUUID();
        var document = new DocumentRecord(documentId, "guide.pdf", "abc", 1, Instant.now());
        var chunk = new Chunk(UUID.randomUUID(), documentId, "guide.pdf", 1, 0, "retrievable text");
        store.save(document, List.of(new EmbeddedChunk(chunk, "test-model", new float[]{1, 2, 3})));
        assertEquals(1, store.listDocuments().size());
        assertArrayEquals(new float[]{1, 2, 3}, store.findAllEmbeddedChunks().getFirst().vector());
        store.deleteDocument(documentId);
        assertTrue(store.findAllEmbeddedChunks().isEmpty());
    }
}
