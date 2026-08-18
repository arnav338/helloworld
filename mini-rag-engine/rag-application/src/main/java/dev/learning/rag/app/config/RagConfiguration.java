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
    @Bean HttpClient modelHttpClient() { return HttpClient.newBuilder().build(); }

    @Bean
    EmbeddingModel embeddingModel(RagProperties properties, HttpClient client, ObjectMapper mapper) {
        var value = properties.embedding();
        return new OpenAiCompatibleEmbeddingModel(
                new OpenAiCompatibleClientConfig(value.baseUrl(), value.apiKey(), value.model(), value.timeout()), client, mapper);
    }

    @Bean
    ChatModel chatModel(RagProperties properties, HttpClient client, ObjectMapper mapper) {
        var value = properties.chat();
        return new OpenAiCompatibleChatModel(
                new OpenAiCompatibleClientConfig(value.baseUrl(), value.apiKey(), value.model(), value.timeout()), client, mapper);
    }

    @Bean VectorStore vectorStore(RagProperties properties) { return new SqliteVectorStore(properties.store().path()); }
    @Bean ParagraphChunker paragraphChunker(RagProperties p) { return new ParagraphChunker(p.chunking().maximumCharacters(), p.chunking().overlapCharacters()); }
    @Bean PdfDocumentExtractor pdfDocumentExtractor() { return new PdfDocumentExtractor(); }
    @Bean Retriever retriever(EmbeddingModel model, VectorStore store) { return new Retriever(model, store); }
    @Bean PromptBuilder promptBuilder() { return new PromptBuilder(); }
    @Bean RagEngine ragEngine(Retriever retriever, PromptBuilder promptBuilder, ChatModel model) { return new RagEngine(retriever, promptBuilder, model); }
}

