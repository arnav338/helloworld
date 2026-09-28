package dev.learning.rag.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learning.rag.core.*;
import dev.learning.rag.document.pdf.PdfDocumentExtractor;
import dev.learning.rag.provider.ChatModel;
import dev.learning.rag.provider.EmbeddingModel;
import dev.learning.rag.provider.openai.*;
import dev.learning.rag.store.VectorStore;
import dev.learning.rag.store.sqlite.SqliteVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

/**
 * The only module that chooses concrete plug-ins.
 *
 * <p>To replace SQLite: add the new store implementation as a Maven dependency,
 * then change the {@code vectorStore} bean return expression. To replace an
 * OpenAI-compatible provider with a native API: add an adapter implementing
 * ChatModel/EmbeddingModel and change only the related bean. Controllers and
 * rag-core remain untouched.</p>
 */
@Configuration
public class RagConfiguration {
    /**
     * Shared outbound HTTP client used by both AI adapters. Spring supplies the
     * same thread-safe instance wherever an {@code HttpClient} is requested.
     *
     * <p>How to evolve it: configure proxy/TLS/connection behavior here, or
     * expose separately qualified clients if chat and embedding require
     * different networking policies. Keep HTTP configuration out of core.</p>
     */
    @Bean
    HttpClient modelHttpClient() {
        return HttpClient.newBuilder().build();
    }

    /**
     * Chooses the V1 implementation of the embedding external-service client.
     * Think of this like selecting a concrete {@code PaymentGateway} bean.
     *
     * <p>V2 seam: implement {@link EmbeddingModel} in another module and return
     * it here. For side-by-side implementations, select by a property/profile
     * or declare qualified beans. Re-index all documents after changing the
     * embedding model because old and new vector spaces are incompatible.</p>
     */
    @Bean
    EmbeddingModel embeddingModel(RagProperties properties, HttpClient client, ObjectMapper mapper) {
        var value = properties.embedding();
        return new OpenAiCompatibleEmbeddingModel(
                new OpenAiCompatibleClientConfig(value.baseUrl(), value.apiKey(), value.model(), value.timeout()), client, mapper);
    }

    /**
     * Chooses the V1 text-generation client. A replacement only needs to
     * implement {@link ChatModel}; retrieval and stored embeddings stay valid.
     */
    @Bean
    ChatModel chatModel(RagProperties properties, HttpClient client, ObjectMapper mapper) {
        var value = properties.chat();
        return new OpenAiCompatibleChatModel(
                new OpenAiCompatibleClientConfig(value.baseUrl(), value.apiKey(), value.model(), value.timeout()), client, mapper);
    }

    /**
     * Selects the repository implementation. To use PostgreSQL/pgvector, add a
     * class implementing {@link VectorStore} and change this factory only.
     */
    @Bean
    VectorStore vectorStore(RagProperties properties) {
        return new SqliteVectorStore(properties.store().path());
    }

    /**
     * Creates the current chunking business component from external settings.
     *
     * <p>V2 seam: for semantic/token chunking, first introduce a small
     * {@code TextChunker} interface, make both versions implement it, inject
     * that interface into the indexing service, and choose the version here.</p>
     */
    @Bean
    ParagraphChunker paragraphChunker(RagProperties properties) {
        return new ParagraphChunker(properties.chunking().maximumCharacters(),
                properties.chunking().overlapCharacters());
    }

    /** Creates the PDFBox infrastructure adapter. Add an extractor interface before supporting OCR side-by-side. */
    @Bean
    PdfDocumentExtractor pdfDocumentExtractor() {
        return new PdfDocumentExtractor();
    }

    /** Creates semantic-search business logic with its two required ports. */
    @Bean
    Retriever retriever(EmbeddingModel model, VectorStore store) {
        return new Retriever(model, store);
    }

    /** Creates the current controlled prompt-formatting component. */
    @Bean
    PromptBuilder promptBuilder() {
        return new PromptBuilder();
    }

    /** Creates the question-answer service by composing retrieval, prompting, and generation. */
    @Bean
    RagEngine ragEngine(Retriever retriever, PromptBuilder promptBuilder, ChatModel model) {
        return new RagEngine(retriever, promptBuilder, model);
    }
}
