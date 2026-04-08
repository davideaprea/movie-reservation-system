package io.github.davideaprea.dto;

import com.mrs.app.booking.dto.BookingResponse;
import com.mrs.app.payment.dto.IntentResponse;
import com.mrs.app.payment.dto.IntentSubmissionResponse;

public record OrderCreateResponse(
        long id,
        BookingResponse booking,
        IntentResponse intent,
        IntentSubmissionResponse gatewayIntent
) {
}
