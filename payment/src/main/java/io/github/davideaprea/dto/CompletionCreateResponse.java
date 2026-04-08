package io.github.davideaprea.dto;

import java.time.LocalDate;

public record CompletionCreateResponse(
        long id,
        LocalDate createdAt,
        String gatewayIntentId,
        long intentId
) {
}
