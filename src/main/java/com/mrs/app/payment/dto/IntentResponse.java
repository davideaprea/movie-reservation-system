package com.mrs.app.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IntentResponse(
        long id,
        String orderId,
        BigDecimal amount,
        LocalDateTime createdAt,
        LocalDateTime expiresAt
) {
}
