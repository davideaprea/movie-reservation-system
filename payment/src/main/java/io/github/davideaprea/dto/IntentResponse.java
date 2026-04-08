package io.github.davideaprea.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IntentResponse(
        String id,
        BigDecimal amount,
        LocalDateTime createdAt,
        LocalDateTime expiresAt
) {
}
