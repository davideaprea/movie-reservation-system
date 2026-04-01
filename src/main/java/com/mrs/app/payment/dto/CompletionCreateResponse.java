package com.mrs.app.payment.dto;

import java.time.LocalDate;

public record CompletionCreateResponse(
        long id,
        LocalDate createdAt,
        String gatewayIntentId,
        long intentId
) {
}
