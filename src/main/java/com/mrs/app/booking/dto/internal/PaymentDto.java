package com.mrs.app.booking.dto.internal;

import com.mrs.app.cinema.dto.projection.SeatProjection;

import java.util.List;

public record PaymentDto(
        long userId,
        List<SeatProjection> selectedSeats
) {
}
