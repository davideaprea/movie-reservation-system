package com.mrs.order.dto;

import com.mrs.booking.dto.BookingResponse;
import com.mrs.payment.dto.IntentGetResponse;

import java.time.LocalDateTime;

public record OrderGetResponse(
        long id,
        LocalDateTime createdAt,
        BookingResponse booking,
        IntentGetResponse intent
) {
}
