package dev.learning.rag.store;

import dev.learning.rag.model.DocumentRecord;
import dev.learning.rag.model.EmbeddedChunk;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Persistence port used by the RAG core.
 *
 * <p>SQLite is the V1 adapter. To add pgvector later, implement these methods
 * in a new module and change only bean wiring. For a large vector database the
 * future contract may add a database-side {@code search} operation; V1 keeps
 * exact similarity in Java so learners can see the algorithm.</p>
 *
 * <p>Study topics: repository pattern, ports and adapters, transactions,
 * exact versus approximate nearest-neighbor search.</p>
 */
public interface VectorStore {
    void save(DocumentRecord document, List<EmbeddedChunk> chunks);
    List<DocumentRecord> listDocuments();
    Optional<DocumentRecord> findDocument(UUID documentId);
    Optional<DocumentRecord> findByChecksum(String checksum);
    List<EmbeddedChunk> findAllEmbeddedChunks();
    void deleteDocument(UUID documentId);
}

