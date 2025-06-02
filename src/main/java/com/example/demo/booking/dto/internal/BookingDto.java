package com.example.demo.booking.dto.internal;

import com.example.demo.cinema.dto.projection.SeatProjection;

import java.util.List;

public record BookingDto(
        List<SeatProjection> selectedSeats,
        long scheduleId,
        long paymentId
) {
}
