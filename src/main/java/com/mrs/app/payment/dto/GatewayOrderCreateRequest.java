package com.mrs.app.payment.dto;

import java.math.BigDecimal;

public record GatewayOrderCreateRequest(
        BigDecimal price,
        String currencyCode
) {
}
