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

/** Thin HTTP adapter: validation and transport conversion only. */
@RestController
@RequestMapping("/api")
public class RagController {
    private final DocumentIndexingService indexingService;
    private final VectorStore store;
    private final Retriever retriever;
    private final RagEngine ragEngine;
    private final RagProperties.Retrieval defaults;

    public RagController(DocumentIndexingService indexingService, VectorStore store, Retriever retriever, RagEngine ragEngine, RagProperties properties) {
        this.indexingService = indexingService; this.store = store; this.retriever = retriever; this.ragEngine = ragEngine; this.defaults = properties.retrieval();
    }

    @PostMapping(value = "/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DocumentRecord upload(@RequestPart("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("uploaded file is empty");
        return indexingService.index(file.getOriginalFilename(), file.getBytes());
    }

    @GetMapping("/documents") public List<DocumentRecord> documents() { return store.listDocuments(); }
    @DeleteMapping("/documents/{id}") public void delete(@PathVariable UUID id) { store.deleteDocument(id); }

    @PostMapping("/search")
    public List<SearchResult> search(@Valid @RequestBody Query request) {
        return retriever.search(request.question(), request.topKOr(defaults.topK()), request.minimumScoreOr(defaults.minimumScore()));
    }

    @PostMapping("/questions")
    public RagAnswer question(@Valid @RequestBody Query request) {
        return ragEngine.answer(request.question(), request.topKOr(defaults.topK()), request.minimumScoreOr(defaults.minimumScore()));
    }

    /** Optional fields allow callers to override retrieval defaults per request. */
    public record Query(
            @NotBlank @Size(max = 4000) String question,
            @Min(1) @Max(50) Integer topK,
            @DecimalMin("-1.0") @DecimalMax("1.0") Double minimumScore) {
        int topKOr(int fallback) { return topK == null ? fallback : topK; }
        double minimumScoreOr(double fallback) { return minimumScore == null ? fallback : minimumScore; }
    }
}

