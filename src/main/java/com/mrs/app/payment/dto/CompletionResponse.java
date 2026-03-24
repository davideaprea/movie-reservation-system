package com.mrs.app.payment.dto;

public record CompletionResponse(
        long id,
        long intentId,
        String gatewayCompletionId
) {
}
