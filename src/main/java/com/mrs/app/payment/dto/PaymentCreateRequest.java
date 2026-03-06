package com.mrs.app.payment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentCreateRequest(
        @Positive
        long userId,

        @NotNull
        @Positive
        BigDecimal totalPrice
) {
}
