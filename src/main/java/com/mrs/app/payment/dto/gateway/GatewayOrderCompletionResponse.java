package com.mrs.app.payment.dto.gateway;

public record GatewayOrderCompletionResponse(
        String id,
        String completionId
) {
}
