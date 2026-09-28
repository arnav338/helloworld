package dev.learning.rag.app.api;

import dev.learning.rag.provider.ModelProviderException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

/**
 * Spring MVC exception layer, equivalent to a central error controller.
 * Service/client/repository exceptions bubble up to this class, which decides
 * the public HTTP status and response DTO. It does not repair or retry errors.
 *
 * <p>How to evolve it: add a narrowly typed handler when a new failure needs a
 * distinct status. Do not expose stack traces, credentials, prompts, or full
 * provider responses. If V2 changes the error JSON contract, introduce a new
 * response DTO/API version rather than silently breaking clients.</p>
 */
@RestControllerAdvice
public class ApiExceptionHandler {
    /** Maps invalid caller input and bean-validation failures to HTTP 400. */
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    ResponseEntity<ApiError> badRequest(Exception exception) {
        return response(HttpStatus.BAD_REQUEST, exception);
    }

    /** Maps an unavailable or invalid model-provider response to HTTP 502. */
    @ExceptionHandler(ModelProviderException.class)
    ResponseEntity<ApiError> providerFailure(Exception exception) {
        return response(HttpStatus.BAD_GATEWAY, exception);
    }

    /**
     * Creates the one stable error shape used by all handlers. Keeping this in
     * one helper prevents different endpoints from returning different fields.
     */
    private ResponseEntity<ApiError> response(HttpStatus status, Exception exception) {
        return ResponseEntity.status(status).body(new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), exception.getMessage()));
    }

    /** HTTP error DTO serialized by Spring/Jackson as JSON. */
    public record ApiError(Instant timestamp, int status, String error, String message) { }
}
