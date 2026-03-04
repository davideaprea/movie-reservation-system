package com.mrs.app.booking.dto;

import com.mrs.app.location.entity.Seat;

import java.util.List;

public record BookingDto(
        List<Seat> selectedSeats,
        long scheduleId,
        long paymentId
) {
}
