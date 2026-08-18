package dev.learning.rag.provider.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import dev.learning.rag.model.ChatRequest;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Contract tests prove the adapters work without Ollama or internet access. */
class OpenAiCompatibleAdaptersTest {
    private HttpServer server;
    private URI baseUrl;

    @BeforeEach void startStubProvider() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        server.createContext("/v1/embeddings", exchange -> json(exchange,
                "{\"data\":[{\"embedding\":[1.0,0.5]},{\"embedding\":[0.2,0.8]}]}"));
        server.createContext("/v1/chat/completions", exchange -> json(exchange,
                "{\"model\":\"stub-chat\",\"choices\":[{\"message\":{\"content\":\"Grounded answer [source 1]\"}}]}"));
        server.start();
        baseUrl = URI.create("http://localhost:" + server.getAddress().getPort() + "/v1");
    }

    @AfterEach void stopStubProvider() { server.stop(0); }

    @Test void parsesEmbeddingBatchInOrder() {
        var model = new OpenAiCompatibleEmbeddingModel(config("stub-embed"), HttpClient.newHttpClient(), new ObjectMapper());
        var vectors = model.embed(List.of("first", "second"));
        assertEquals(2, vectors.size());
        assertArrayEquals(new float[]{1.0f, 0.5f}, vectors.getFirst());
    }

    @Test void parsesChatCompletion() {
        var model = new OpenAiCompatibleChatModel(config("stub-chat"), HttpClient.newHttpClient(), new ObjectMapper());
        var response = model.generate(new ChatRequest("Use evidence", "Question and evidence"));
        assertEquals("Grounded answer [source 1]", response.content());
        assertEquals("stub-chat", response.model());
    }

    private OpenAiCompatibleClientConfig config(String model) {
        return new OpenAiCompatibleClientConfig(baseUrl, "test-key", model, Duration.ofSeconds(2));
    }

    private static void json(HttpExchange exchange, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }
}
