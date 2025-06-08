package com.mrs.app.booking.dto.internal;

import com.mrs.app.cinema.enumeration.SeatType;

import java.util.List;

public record PaymentDto(
        long userId,
        List<SeatType> selectedSeatsType
) {
}
