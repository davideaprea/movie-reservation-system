package com.mrs.app.payment.dto;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public record PaymentUpdateRequest(
        long userId,
        long paymentId
) {
}
