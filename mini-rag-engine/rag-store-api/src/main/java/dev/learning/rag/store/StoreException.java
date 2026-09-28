package dev.learning.rag.store;

/**
 * Repository exception boundary. Database implementations translate JDBC,
 * network, or serialization failures so service/controller code remains
 * independent of a particular persistence technology.
 */
public final class StoreException extends RuntimeException {
    /** Wraps a low-level cause while preserving it for diagnostics. */
    public StoreException(String message, Throwable cause) {
        super(message, cause);
    }

    /** Represents a repository failure that has no lower-level exception. */
    public StoreException(String message) {
        super(message);
    }
}
