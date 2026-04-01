package com.mrs.app.order.dto;

import com.mrs.app.booking.dto.BookingResponse;
import com.mrs.app.payment.dto.IntentResponse;

public record OrderCreateResponse(
        String id,
        BookingResponse booking,
        IntentResponse payment
) {
}
