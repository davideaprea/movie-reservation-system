package com.mrs.app.booking.dto.internal;

import com.mrs.app.cinema.entity.Seat;

import java.util.List;

public record BookingDto(
        List<Seat> selectedSeats,
        long scheduleId,
        long paymentId
) {
}
