package com.mrs.app.order.dto;

import com.mrs.app.payment.dto.PaymentResponse;

public record OrderUpdateResponse(
        long id,
        long userId,
        PaymentResponse payment
) {
}
