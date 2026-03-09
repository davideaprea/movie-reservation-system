package com.mrs.app.payment.dto;

import com.mrs.app.payment.enumeration.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        long id,
        long userId,
        LocalDateTime createdAt,
        PaymentStatus status,
        GatewayOrderDTO gatewayOrder
) {
    public record GatewayOrderDTO(
            String id,
            String completionId,
            BigDecimal price
    ) {
    }
}
