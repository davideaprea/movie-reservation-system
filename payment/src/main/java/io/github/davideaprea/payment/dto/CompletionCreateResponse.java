package io.github.davideaprea.payment.dto;

import java.time.LocalDate;

public record CompletionCreateResponse(
        long id,
        LocalDate createdAt,
        String gatewayIntentId,
        long intentId
) {
}
