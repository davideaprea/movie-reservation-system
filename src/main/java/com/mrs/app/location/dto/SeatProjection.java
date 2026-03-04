package com.mrs.app.location.dto;

import com.mrs.app.location.enumeration.SeatType;

public record SeatProjection(
        long id,
        SeatType type,
        int rowNumber,
        int seatNumber,
        long hallId
) {
}
