package com.mrs.app.order.dto;

import com.mrs.app.booking.dto.BookingResponse;
import com.mrs.app.payment.dto.IntentGetResponse;

import java.time.LocalDateTime;

public record OrderGetResponse(
        long id,
        LocalDateTime createdAt,
        BookingResponse booking,
        IntentGetResponse intent
) {
}
