package io.github.davideaprea.payment.dto;

public record CompletionCreateRequest(
        String gatewayIntentId,
        String internalIntentId
) {
}
