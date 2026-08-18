package dev.learning.rag.provider;

/** Converts transport/provider failures into one application-level exception. */
public final class ModelProviderException extends RuntimeException {
    public ModelProviderException(String message) { super(message); }
    public ModelProviderException(String message, Throwable cause) { super(message, cause); }
}

