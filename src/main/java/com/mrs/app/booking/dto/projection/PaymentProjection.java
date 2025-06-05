package com.mrs.app.booking.dto.projection;

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
