package com.mrs.app.payment.dto;

import java.math.BigDecimal;

public record PaymentGatewayOrderRequest(
        BigDecimal price,
        String currencyCode
) {
}
