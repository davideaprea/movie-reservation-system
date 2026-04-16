package com.mrs.app.payment.dto.gateway;

public record GatewayRefundCreateRequest(
        String gatewayCompletionId,
        String internalCompletionId
) {
}
