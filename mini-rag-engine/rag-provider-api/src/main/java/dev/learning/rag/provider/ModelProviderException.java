package dev.learning.rag.provider;

/**
 * External-client exception boundary. Adapters translate HTTP/JSON/provider
 * failures into this type so controllers do not depend on provider libraries.
 */
public final class ModelProviderException extends RuntimeException {
    /** Creates a provider failure when there is no useful lower-level cause. */
    public ModelProviderException(String message) {
        super(message);
    }

    /** Preserves the technical cause for logs while exposing one stable type. */
    public ModelProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
