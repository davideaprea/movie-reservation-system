package io.github.davideaprea.dto;

public record CompletionCreateRequest(
        String gatewayIntentId,
        String internalIntentId
) {
}
