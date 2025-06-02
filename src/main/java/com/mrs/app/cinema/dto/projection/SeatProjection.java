package com.mrs.app.cinema.dto.projection;

import com.mrs.app.cinema.enumeration.SeatType;

public record SeatProjection(
        long id,
        SeatType type,
        int rowNumber,
        int seatNumber,
        long hallId
) {
}
