package com.mrs.app.order.dto;

import com.mrs.app.booking.dto.BookingResponse;
import com.mrs.app.payment.dto.IntentCreateResponse;

public record OrderCreateResponse(
        String id,
        BookingResponse booking,
        IntentCreateResponse intent
) {
}
