package com.mrs.app.payment.dto;

public record CompletionCreateRequest(
        String intentId,
        String orderId
) {
}
