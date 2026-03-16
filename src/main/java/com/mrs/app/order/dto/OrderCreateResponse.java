package com.mrs.app.order.dto;

import com.mrs.app.booking.dto.BookingResponse;
import com.mrs.app.payment.dto.PaymentResponse;

import java.time.LocalDateTime;
import java.util.List;

public record OrderCreateResponse(
        long id,
        List<BookingResponse> bookings,
        PaymentResponse payment
) {
}
