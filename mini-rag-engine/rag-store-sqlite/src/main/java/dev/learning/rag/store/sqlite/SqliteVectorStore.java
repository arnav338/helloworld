package dev.learning.rag.store.sqlite;

import dev.learning.rag.model.Chunk;
import dev.learning.rag.model.DocumentRecord;
import dev.learning.rag.model.EmbeddedChunk;
import dev.learning.rag.store.StoreException;
import dev.learning.rag.store.VectorStore;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * File-backed SQLite implementation of {@link VectorStore}.
 *
 * <p>Each operation opens a short-lived JDBC connection. SQLite remains an
 * embedded library: there is no networked database process or password. The
 * document and all chunks are inserted in one transaction, preventing a
 * partially indexed document after a failure.</p>
 *
 * <p>Study topics: JDBC prepared statements, ACID transactions, foreign keys,
 * WAL journaling, relational normalization, try-with-resources.</p>
 */
public final class SqliteVectorStore implements VectorStore {
    private final String jdbcUrl;

    public SqliteVectorStore(Path databasePath) {
        try {
            Path absolute = databasePath.toAbsolutePath().normalize();
            if (absolute.getParent() != null) Files.createDirectories(absolute.getParent());
            this.jdbcUrl = "jdbc:sqlite:" + absolute;
            initializeSchema();
        } catch (Exception exception) {
            throw new StoreException("failed to initialize SQLite store", exception);
        }
    }

    private Connection open() throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcUrl);
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
            statement.execute("PRAGMA journal_mode = WAL");
        }
        return connection;
    }

    private void initializeSchema() throws SQLException {
        try (Connection connection = open(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS documents (
                      id TEXT PRIMARY KEY, filename TEXT NOT NULL, checksum TEXT NOT NULL UNIQUE,
                      page_count INTEGER NOT NULL, created_at TEXT NOT NULL)
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS chunks (
                      id TEXT PRIMARY KEY, document_id TEXT NOT NULL, filename TEXT NOT NULL,
                      page_number INTEGER NOT NULL, chunk_index INTEGER NOT NULL, text TEXT NOT NULL,
                      FOREIGN KEY(document_id) REFERENCES documents(id) ON DELETE CASCADE)
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS embeddings (
                      chunk_id TEXT PRIMARY KEY, model TEXT NOT NULL, dimension INTEGER NOT NULL, vector BLOB NOT NULL,
                      FOREIGN KEY(chunk_id) REFERENCES chunks(id) ON DELETE CASCADE)
                    """);
            statement.executeUpdate("CREATE INDEX IF NOT EXISTS idx_chunks_document ON chunks(document_id)");
        }
    }

    @Override
    public void save(DocumentRecord document, List<EmbeddedChunk> chunks) {
        if (chunks.stream().anyMatch(item -> !item.chunk().documentId().equals(document.id()))) {
            throw new IllegalArgumentException("every chunk must belong to the saved document");
        }
        try (Connection connection = open()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement statement = connection.prepareStatement("INSERT INTO documents(id,filename,checksum,page_count,created_at) VALUES(?,?,?,?,?)")) {
                    statement.setString(1, document.id().toString()); statement.setString(2, document.filename());
                    statement.setString(3, document.checksum()); statement.setInt(4, document.pageCount());
                    statement.setString(5, document.createdAt().toString()); statement.executeUpdate();
                }
                try (PreparedStatement chunkStatement = connection.prepareStatement("INSERT INTO chunks(id,document_id,filename,page_number,chunk_index,text) VALUES(?,?,?,?,?,?)");
                     PreparedStatement vectorStatement = connection.prepareStatement("INSERT INTO embeddings(chunk_id,model,dimension,vector) VALUES(?,?,?,?)")) {
                    for (EmbeddedChunk item : chunks) {
                        Chunk chunk = item.chunk();
                        chunkStatement.setString(1, chunk.id().toString()); chunkStatement.setString(2, chunk.documentId().toString());
                        chunkStatement.setString(3, chunk.filename()); chunkStatement.setInt(4, chunk.pageNumber());
                        chunkStatement.setInt(5, chunk.chunkIndex()); chunkStatement.setString(6, chunk.text()); chunkStatement.addBatch();
                        float[] vector = item.vector();
                        vectorStatement.setString(1, chunk.id().toString()); vectorStatement.setString(2, item.embeddingModel());
                        vectorStatement.setInt(3, vector.length); vectorStatement.setBytes(4, FloatVectorCodec.encode(vector)); vectorStatement.addBatch();
                    }
                    chunkStatement.executeBatch(); vectorStatement.executeBatch();
                }
                connection.commit();
            } catch (Exception exception) {
                connection.rollback();
                throw exception;
            }
        } catch (Exception exception) {
            throw new StoreException("failed to save document " + document.filename(), exception);
        }
    }

    @Override public List<DocumentRecord> listDocuments() {
        List<DocumentRecord> result = new ArrayList<>();
        try (Connection connection = open(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM documents ORDER BY created_at DESC"); ResultSet rows = statement.executeQuery()) {
            while (rows.next()) result.add(readDocument(rows));
            return List.copyOf(result);
        } catch (SQLException exception) { throw new StoreException("failed to list documents", exception); }
    }

    @Override public Optional<DocumentRecord> findDocument(UUID documentId) { return findDocumentBy("id", documentId.toString()); }
    @Override public Optional<DocumentRecord> findByChecksum(String checksum) { return findDocumentBy("checksum", checksum); }

    private Optional<DocumentRecord> findDocumentBy(String column, String value) {
        // column is selected only by private callers, never user input. Values
        // remain parameterized to prevent SQL injection.
        try (Connection connection = open(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM documents WHERE " + column + " = ?")) {
            statement.setString(1, value);
            try (ResultSet rows = statement.executeQuery()) { return rows.next() ? Optional.of(readDocument(rows)) : Optional.empty(); }
        } catch (SQLException exception) { throw new StoreException("failed to find document", exception); }
    }

    @Override public List<EmbeddedChunk> findAllEmbeddedChunks() {
        String sql = """
                SELECT c.id,c.document_id,c.filename,c.page_number,c.chunk_index,c.text,
                       e.model,e.dimension,e.vector
                FROM chunks c JOIN embeddings e ON e.chunk_id=c.id ORDER BY c.document_id,c.chunk_index
                """;
        List<EmbeddedChunk> result = new ArrayList<>();
        try (Connection connection = open(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet rows = statement.executeQuery()) {
            while (rows.next()) {
                Chunk chunk = new Chunk(UUID.fromString(rows.getString("id")), UUID.fromString(rows.getString("document_id")),
                        rows.getString("filename"), rows.getInt("page_number"), rows.getInt("chunk_index"), rows.getString("text"));
                result.add(new EmbeddedChunk(chunk, rows.getString("model"),
                        FloatVectorCodec.decode(rows.getBytes("vector"), rows.getInt("dimension"))));
            }
            return List.copyOf(result);
        } catch (SQLException exception) { throw new StoreException("failed to load vectors", exception); }
    }

    @Override public void deleteDocument(UUID documentId) {
        try (Connection connection = open(); PreparedStatement statement = connection.prepareStatement("DELETE FROM documents WHERE id=?")) {
            statement.setString(1, documentId.toString()); statement.executeUpdate();
        } catch (SQLException exception) { throw new StoreException("failed to delete document", exception); }
    }

    private static DocumentRecord readDocument(ResultSet rows) throws SQLException {
        return new DocumentRecord(UUID.fromString(rows.getString("id")), rows.getString("filename"), rows.getString("checksum"), rows.getInt("page_count"), Instant.parse(rows.getString("created_at")));
    }
}

