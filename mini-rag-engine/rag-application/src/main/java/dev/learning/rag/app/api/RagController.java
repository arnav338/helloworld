package dev.learning.rag.app.api;

import dev.learning.rag.app.config.RagProperties;
import dev.learning.rag.app.service.DocumentIndexingService;
import dev.learning.rag.core.RagEngine;
import dev.learning.rag.core.Retriever;
import dev.learning.rag.model.*;
import dev.learning.rag.store.VectorStore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * MVC controller: the front door of the backend.
 *
 * <p>Its responsibility is deliberately limited to HTTP concerns: deserialize
 * requests, apply validation, choose request/default parameters, call the
 * service or repository contract, and serialize the result. RAG algorithms do
 * not belong here, just as pricing or payment rules would not belong in a
 * normal Spring controller.</p>
 *
 * <p>How to evolve it: add a new endpoint only when the HTTP capability
 * changes. Improve indexing in {@link DocumentIndexingService}, retrieval in
 * {@link Retriever}, and answer generation in {@link RagEngine}; those changes
 * should not require controller edits.</p>
 */
@RestController
@RequestMapping("/api")
public class RagController {
    private final DocumentIndexingService indexingService;
    private final VectorStore store;
    private final Retriever retriever;
    private final RagEngine ragEngine;
    private final RagProperties.Retrieval defaults;

    /**
     * Constructor injection makes dependencies visible and testable. Spring
     * obtains the concrete objects from {@code RagConfiguration}.
     *
     * <p>V2 seam: if retrieval or answering later has multiple implementations,
     * depend here on a small interface and let configuration select V1/V2.</p>
     */
    public RagController(DocumentIndexingService indexingService, VectorStore store,
                         Retriever retriever, RagEngine ragEngine, RagProperties properties) {
        this.indexingService = indexingService;
        this.store = store;
        this.retriever = retriever;
        this.ragEngine = ragEngine;
        this.defaults = properties.retrieval();
    }

    /**
     * Controller flow: reject an empty multipart part, convert it to plain Java
     * values, and delegate the complete use case to the indexing service.
     *
     * <p>How to evolve it: support a new upload type through a new use case or
     * a generic document command; do not add PDF parsing/model calls here.</p>
     */
    @PostMapping(value = "/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DocumentRecord upload(@RequestPart("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("uploaded file is empty");
        return indexingService.index(file.getOriginalFilename(), file.getBytes());
    }

    /** Lists indexed document metadata through the repository abstraction. */
    @GetMapping("/documents")
    public List<DocumentRecord> documents() {
        return store.listDocuments();
    }

    /**
     * Deletes a document. SQLite foreign-key cascade removes its chunks and
     * vectors. A future service can wrap this if authorization/auditing appears.
     */
    @DeleteMapping("/documents/{id}")
    public void delete(@PathVariable UUID id) {
        store.deleteDocument(id);
    }

    /**
     * Runs only semantic retrieval and returns ranked passages. This endpoint
     * is the equivalent of inspecting repository/service results before a
     * presentation layer transforms them, and is the first debugging tool for
     * weak answers.
     */
    @PostMapping("/search")
    public List<SearchResult> search(@Valid @RequestBody Query request) {
        return retriever.search(request.question(), request.topKOr(defaults.topK()), request.minimumScoreOr(defaults.minimumScore()));
    }

    /** Runs the full service flow: retrieve evidence and ask the chat model. */
    @PostMapping("/questions")
    public RagAnswer question(@Valid @RequestBody Query request) {
        return ragEngine.answer(request.question(), request.topKOr(defaults.topK()), request.minimumScoreOr(defaults.minimumScore()));
    }

    /**
     * Request DTO. Bean Validation protects the service boundary; nullable
     * tuning fields mean "use configured defaults", not zero.
     *
     * <p>How to evolve it: adding optional tuning values is backward compatible.
     * Incompatible request semantics should use a versioned endpoint/DTO.</p>
     */
    public record Query(
            @NotBlank @Size(max = 4000) String question,
            @Min(1) @Max(50) Integer topK,
            @DecimalMin("-1.0") @DecimalMax("1.0") Double minimumScore) {
        /** Returns the caller's result limit or the application default. */
        int topKOr(int fallback) {
            return topK == null ? fallback : topK;
        }

        /** Returns the caller's similarity cutoff or the application default. */
        double minimumScoreOr(double fallback) {
            return minimumScore == null ? fallback : minimumScore;
        }
    }
}
