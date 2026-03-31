package com.mrs.app.payment.dto;

import java.math.BigDecimal;

public record PaymentResponse(
        long id,
        String gatewayOrderId,
        BigDecimal price
) {
}
