package com.mrs.app.payment.dto.gateway;

import java.math.BigDecimal;

public record GatewayIntentCreateRequest(
        BigDecimal price,
        String currencyCode
) {
}
