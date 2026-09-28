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
    /**
     * Atomically persists one document aggregate: parent metadata plus all
     * chunks/vectors. Implementations must not leave partial data on failure.
     */
    void save(DocumentRecord document, List<EmbeddedChunk> chunks);

    /** Returns document metadata for the controller, newest first if supported. */
    List<DocumentRecord> listDocuments();

    /** Finds one document by its public UUID without loading all chunk bodies. */
    Optional<DocumentRecord> findDocument(UUID documentId);

    /** Supports idempotent upload by looking up a deterministic content hash. */
    Optional<DocumentRecord> findByChecksum(String checksum);

    /**
     * Returns V1 retrieval candidates with their text and vectors. A scalable
     * V2 store may add a database-side search contract instead of loading all.
     */
    List<EmbeddedChunk> findAllEmbeddedChunks();

    /** Deletes the document aggregate, including chunks/vectors by cascade. */
    void deleteDocument(UUID documentId);
}
