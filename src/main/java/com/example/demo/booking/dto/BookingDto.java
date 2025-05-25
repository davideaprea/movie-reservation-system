package com.example.demo.booking.dto;

import com.example.demo.cinema.projection.SeatProjection;

import java.util.List;

public record BookingDto(
        List<SeatProjection> selectedSeats,
        long scheduleId,
        long paymentId
) {
}
