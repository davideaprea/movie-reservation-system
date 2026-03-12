package com.mrs.app.order.dto;

import com.mrs.app.booking.dto.BookingCreateResponse;
import com.mrs.app.payment.dto.PaymentResponse;

import java.time.LocalDateTime;
import java.util.List;

public record OrderCreateResponse(
        long id,
        LocalDateTime createdAt,
        List<BookingCreateResponse> bookings,
        PaymentResponse payment
) {
}
