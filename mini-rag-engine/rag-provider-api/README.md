# rag-provider-api

## Purpose

Defines the two ports through which the core uses AI models: `EmbeddingModel` and `ChatModel`. It also supplies `ModelProviderException`, the stable error boundary seen by the application.

## Call contracts

`EmbeddingModel.embed` must preserve input order, return exactly one non-empty vector per input, and keep dimensions consistent. `modelName` must be stable because it is persisted. `ChatModel.generate` accepts neutral system/user text and returns neutral content/model metadata.

## Adding a provider

Create a separate adapter module, depend on this module, implement one or both interfaces, validate provider output, write contract tests, and select the implementation in `RagConfiguration`. Do not add provider switches or JSON DTOs here.

## Maintenance

Interface changes affect every provider. Prefer adding adapter-local options over expanding a general interface. If a capability is truly shared, document its semantics before changing the contract.

## Study topics

Dependency inversion, ports and adapters, strategy pattern, service-provider abstractions, embeddings, model inference, retries, timeouts, and API error normalization.

