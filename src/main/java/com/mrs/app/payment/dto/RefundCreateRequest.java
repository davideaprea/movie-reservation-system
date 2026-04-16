package com.mrs.app.payment.dto;

public record RefundCreateRequest(
        String completionId,
        String gatewayRefundId
) {
}
