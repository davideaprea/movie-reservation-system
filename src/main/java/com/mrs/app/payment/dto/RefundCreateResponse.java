package com.mrs.app.payment.dto;

import java.time.LocalDateTime;

public record RefundCreateResponse(
        long id,
        String completionId,
        String gatewayRefundId,
        LocalDateTime createdAt
) {
}
