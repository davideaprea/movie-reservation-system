package com.mrs.app.payment.dto;

public record CompletionCreateRequest(
        String gatewayIntentId,
        String internalIntentId
) {
}
