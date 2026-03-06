package com.mrs.app.payment.dto;

public record PaymentCompletionRequest(
        long userId,
        long paymentId
) {
}
