package com.mrs.app.order.dto;

import com.mrs.app.booking.dto.BookingResponse;
import com.mrs.app.payment.dto.PaymentResponse;

public record OrderCreateResponse(
        long id,
        BookingResponse booking,
        PaymentResponse payment
) {
}
