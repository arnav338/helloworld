package dev.learning.rag.store;

/** Consistent unchecked boundary for database and serialization failures. */
public final class StoreException extends RuntimeException {
    public StoreException(String message, Throwable cause) { super(message, cause); }
    public StoreException(String message) { super(message); }
}
