package io.github.davideaprea.payment.dto.gateway;

import java.math.BigDecimal;

public record GatewayIntentCreateRequest(
        BigDecimal price,
        String currencyCode,
        String key
) {
}
