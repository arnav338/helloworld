package dev.learning.rag.provider.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.learning.rag.model.ChatRequest;
import dev.learning.rag.model.ChatResponse;
import dev.learning.rag.provider.ChatModel;
import dev.learning.rag.provider.ModelProviderException;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/** OpenAI-compatible {@code /chat/completions} adapter; no provider SDK required. */
public final class OpenAiCompatibleChatModel implements ChatModel {
    private final OpenAiCompatibleClientConfig config;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OpenAiCompatibleChatModel(OpenAiCompatibleClientConfig config, HttpClient httpClient, ObjectMapper objectMapper) {
        this.config = config;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Converts our two-field provider-neutral request into chat messages.
     * Temperature zero reduces variability for document QA; it is not a truth
     * guarantee. Study topics: chat roles, temperature, deterministic decoding.
     */
    @Override
    public ChatResponse generate(ChatRequest request) {
        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("model", config.model());
        payload.put("temperature", 0.0);
        ArrayNode messages = payload.putArray("messages");
        messages.addObject().put("role", "system").put("content", request.systemInstruction());
        messages.addObject().put("role", "user").put("content", request.userPrompt());

        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder(config.endpoint("chat/completions"))
                    .timeout(config.timeout()).header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(payload)));
            if (!config.apiKey().isBlank()) builder.header("Authorization", "Bearer " + config.apiKey());
            HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) throw new ModelProviderException("chat provider returned HTTP " + response.statusCode());
            JsonNode root = objectMapper.readTree(response.body());
            String content = root.path("choices").path(0).path("message").path("content").asText();
            if (content.isBlank()) throw new ModelProviderException("chat provider returned no message content");
            return new ChatResponse(content, root.path("model").asText(config.model()));
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ModelProviderException("chat request was interrupted", exception);
        } catch (IOException exception) {
            throw new ModelProviderException("chat request failed", exception);
        }
    }
}
