package dev.learning.rag.app.api;

import dev.learning.rag.provider.ModelProviderException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

/** Converts expected failures into stable JSON rather than HTML stack traces. */
@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    ResponseEntity<ApiError> badRequest(Exception exception) { return response(HttpStatus.BAD_REQUEST, exception); }

    @ExceptionHandler(ModelProviderException.class)
    ResponseEntity<ApiError> providerFailure(Exception exception) { return response(HttpStatus.BAD_GATEWAY, exception); }

    private ResponseEntity<ApiError> response(HttpStatus status, Exception exception) {
        return ResponseEntity.status(status).body(new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), exception.getMessage()));
    }

    public record ApiError(Instant timestamp, int status, String error, String message) { }
}

