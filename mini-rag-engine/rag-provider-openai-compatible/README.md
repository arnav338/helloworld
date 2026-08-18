# rag-provider-openai-compatible

## Purpose

Implements chat and embeddings over the commonly supported OpenAI-compatible HTTP shape using Java `HttpClient` and Jackson. Ollama is the default server, but no Ollama-specific SDK is used.

## Endpoints

- `POST {baseUrl}/embeddings`
- `POST {baseUrl}/chat/completions`

The adapter adds a bearer header only when a key is non-blank. It validates status codes, vector counts, dimensions, and response content.

## Change server without code

Set chat and embedding base URL, model, key, and timeout independently. Re-index if the embedding model changes. A server that only implements chat can be combined with a different embedding server.

## Add a native provider

Do not complicate this adapter with unrelated response formats. Create another module implementing the provider API, then switch beans in the application composition root.

## Maintenance

Never log authorization headers or complete document prompts. Bound error response text. Preserve interrupt status. Add a stub-server contract test before changing request JSON.

## Study topics

HTTP request construction, JSON trees, bearer authentication, API compatibility layers, adapter pattern, request batching, status handling, and interruption.

