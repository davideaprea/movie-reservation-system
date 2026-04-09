package com.mrs.app.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IntentGetResponse(
        String id,
        BigDecimal amount,
        LocalDateTime createdAt,
        LocalDateTime expiresAt,
        CompletionResponse completion
) {
    public record CompletionResponse(
            long id,
            String gatewayIntentId,
            LocalDateTime createdAt
    ) {
    }
}
