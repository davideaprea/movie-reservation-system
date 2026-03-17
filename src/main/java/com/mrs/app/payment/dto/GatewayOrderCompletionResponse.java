package com.mrs.app.payment.dto;

public record GatewayOrderCompletionResponse(
        String id,
        String completionId
) {
}
