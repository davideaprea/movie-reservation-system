package com.mrs.app.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        long id,
        long userId,
        LocalDateTime createdAt,
        GatewayOrderDTO gatewayOrder
) {
    public record GatewayOrderDTO(
            String id,
            String completionId,
            BigDecimal price
    ) {
    }
}
