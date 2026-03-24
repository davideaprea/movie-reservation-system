package com.mrs.app.payment.dto;

import java.math.BigDecimal;

public record IntentResponse(
        long id,
        String gatewayOrderId,
        BigDecimal price
) {
}
