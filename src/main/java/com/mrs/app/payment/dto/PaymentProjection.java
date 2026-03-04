package com.mrs.app.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentProjection(
        long id,
        String orderId,
        String captureId,
        BigDecimal price,
        long userId,
        LocalDateTime createdAt
) {
}
