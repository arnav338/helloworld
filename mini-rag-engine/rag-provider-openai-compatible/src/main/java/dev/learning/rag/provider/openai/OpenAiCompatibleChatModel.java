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

/**
 * Infrastructure/client implementation of {@link ChatModel}. It plays the same
 * role as a REST client adapter in a normal backend: translate domain DTO to
 * provider JSON, send HTTP, validate JSON, translate back to a domain DTO.
 *
 * <p>How to evolve it: changes specific to the OpenAI-compatible contract stay
 * here. A native Anthropic/OCI/other API should be a separate implementation of
 * {@code ChatModel}, selected in {@code RagConfiguration}, not an if/else here.</p>
 */
public final class OpenAiCompatibleChatModel implements ChatModel {
    private final OpenAiCompatibleClientConfig config;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    /** Receives immutable settings plus reusable HTTP/JSON infrastructure. */
    public OpenAiCompatibleChatModel(OpenAiCompatibleClientConfig config, HttpClient httpClient,
                                     ObjectMapper objectMapper) {
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
        // Translation layer: provider-neutral ChatRequest becomes the JSON body
        // required by the OpenAI-compatible chat-completions contract.
        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("model", config.model());
        payload.put("temperature", 0.0);
        ArrayNode messages = payload.putArray("messages");
        messages.addObject().put("role", "system").put("content", request.systemInstruction());
        messages.addObject().put("role", "user").put("content", request.userPrompt());

        try {
            // Transport layer: endpoint, timeout, content type, optional bearer
            // authentication, and POST body are infrastructure concerns.
            HttpRequest.Builder builder = HttpRequest.newBuilder(config.endpoint("chat/completions"))
                    .timeout(config.timeout()).header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(payload)));
            if (!config.apiKey().isBlank()) builder.header("Authorization", "Bearer " + config.apiKey());
            HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            // A non-2xx status is a downstream dependency failure, not a bad
            // request to our own controller.
            if (response.statusCode() / 100 != 2) throw new ModelProviderException("chat provider returned HTTP " + response.statusCode());

            // Translate only the fields promised by our interface; provider
            // metadata does not leak into the service layer.
            JsonNode root = objectMapper.readTree(response.body());
            String content = root.path("choices").path(0).path("message").path("content").asText();
            if (content.isBlank()) throw new ModelProviderException("chat provider returned no message content");
            return new ChatResponse(content, root.path("model").asText(config.model()));
        } catch (InterruptedException exception) {
            // Preserve cancellation semantics before translating the exception.
            Thread.currentThread().interrupt();
            throw new ModelProviderException("chat request was interrupted", exception);
        } catch (IOException exception) {
            throw new ModelProviderException("chat request failed", exception);
        }
    }
}
