package com.mrs.app.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IntentResponse(
        String id,
        BigDecimal amount,
        LocalDateTime createdAt,
        LocalDateTime expiresAt
) {
}
