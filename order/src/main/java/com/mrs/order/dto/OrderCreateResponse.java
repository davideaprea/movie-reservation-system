package com.mrs.order.dto;

import com.mrs.booking.dto.BookingResponse;
import com.mrs.payment.dto.IntentCreateResponse;
import com.mrs.payment.dto.IntentSubmissionResponse;

public record OrderCreateResponse(
        long id,
        BookingResponse booking,
        IntentCreateResponse intent,
        IntentSubmissionResponse gatewayIntent
) {
}
