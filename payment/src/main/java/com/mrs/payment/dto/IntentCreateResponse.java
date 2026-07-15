package com.mrs.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IntentCreateResponse(
        String id,
        BigDecimal amount,
        LocalDateTime createdAt,
        LocalDateTime expiresAt
) {
}
