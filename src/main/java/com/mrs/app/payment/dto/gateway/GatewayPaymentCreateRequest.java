package com.mrs.app.payment.dto.gateway;

import java.math.BigDecimal;

public record GatewayPaymentCreateRequest(
        BigDecimal price,
        String currencyCode,
        String key
) {
}
