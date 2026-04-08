package io.github.davideaprea.order.dto;

import io.github.davideaprea.booking.dto.BookingResponse;
import io.github.davideaprea.payment.dto.IntentResponse;
import io.github.davideaprea.payment.dto.IntentSubmissionResponse;

public record OrderCreateResponse(
        long id,
        BookingResponse booking,
        IntentResponse intent,
        IntentSubmissionResponse gatewayIntent
) {
}
