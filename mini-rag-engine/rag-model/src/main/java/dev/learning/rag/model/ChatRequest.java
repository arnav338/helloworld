package dev.learning.rag.model;

/**
 * Provider-neutral request for answer generation. The system instruction and
 * user prompt remain separate because chat APIs assign them different trust.
 */
public record ChatRequest(String systemInstruction, String userPrompt) {
    public ChatRequest {
        if (systemInstruction == null || systemInstruction.isBlank()) throw new IllegalArgumentException("systemInstruction is required");
        if (userPrompt == null || userPrompt.isBlank()) throw new IllegalArgumentException("userPrompt is required");
    }
}

