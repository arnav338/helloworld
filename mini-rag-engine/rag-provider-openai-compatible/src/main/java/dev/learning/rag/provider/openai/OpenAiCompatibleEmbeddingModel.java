package dev.learning.rag.provider.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.learning.rag.provider.EmbeddingModel;
import dev.learning.rag.provider.ModelProviderException;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Calls the OpenAI-compatible {@code POST /embeddings} contract using only the
 * JDK HTTP client and Jackson. Ollama is the default target, but changing base
 * URL, model and API key can target another compatible server.
 *
 * <p>Study topics: HTTP JSON APIs, OpenAI API compatibility, Jackson tree
 * model, batching, response validation, adapter pattern.</p>
 */
public final class OpenAiCompatibleEmbeddingModel implements EmbeddingModel {
    private final OpenAiCompatibleClientConfig config;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OpenAiCompatibleEmbeddingModel(OpenAiCompatibleClientConfig config, HttpClient httpClient, ObjectMapper objectMapper) {
        this.config = config;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<float[]> embed(List<String> inputs) {
        if (inputs == null || inputs.isEmpty() || inputs.stream().anyMatch(value -> value == null || value.isBlank())) {
            throw new IllegalArgumentException("embedding inputs must not be empty or blank");
        }
        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("model", config.model());
        payload.set("input", objectMapper.valueToTree(inputs));
        JsonNode root = send(payload, "embeddings");
        JsonNode data = root.path("data");
        if (!data.isArray() || data.size() != inputs.size()) {
            throw new ModelProviderException("embedding provider returned " + data.size() + " vectors for " + inputs.size() + " inputs");
        }

        List<float[]> vectors = new ArrayList<>();
        int dimension = -1;
        for (JsonNode item : data) {
            JsonNode embedding = item.path("embedding");
            if (!embedding.isArray() || embedding.isEmpty()) throw new ModelProviderException("provider returned an empty embedding");
            float[] vector = new float[embedding.size()];
            for (int index = 0; index < vector.length; index++) vector[index] = embedding.get(index).floatValue();
            if (dimension == -1) dimension = vector.length;
            if (vector.length != dimension) throw new ModelProviderException("provider returned inconsistent vector dimensions");
            vectors.add(vector);
        }
        return List.copyOf(vectors);
    }

    @Override public String modelName() { return config.model(); }

    private JsonNode send(JsonNode payload, String route) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder(config.endpoint(route))
                    .timeout(config.timeout()).header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(payload)));
            if (!config.apiKey().isBlank()) builder.header("Authorization", "Bearer " + config.apiKey());
            HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                throw new ModelProviderException("embedding provider returned HTTP " + response.statusCode() + ": " + abbreviate(response.body()));
            }
            return objectMapper.readTree(response.body());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ModelProviderException("embedding request was interrupted", exception);
        } catch (IOException exception) {
            throw new ModelProviderException("embedding request failed", exception);
        }
    }

    private static String abbreviate(String value) {
        if (value == null) return "";
        return value.length() <= 500 ? value : value.substring(0, 500) + "...";
    }
}

