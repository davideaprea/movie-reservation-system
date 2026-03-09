package com.mrs.app.payment.dto;

public record PaymentUpdateRequest(
        long userId,
        long paymentId
) {
}
