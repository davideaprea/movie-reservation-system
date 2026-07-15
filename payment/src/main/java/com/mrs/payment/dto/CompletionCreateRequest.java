package com.mrs.payment.dto;

public record CompletionCreateRequest(
        String gatewayIntentId,
        String internalIntentId
) {
}
