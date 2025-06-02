package com.mrs.app.cinema.dto.projection;

import com.mrs.app.cinema.enumeration.SeatType;

public record ScheduleSeatDetails(
        long id,
        SeatType type,
        int rowNumber,
        int seatNumber,
        boolean isAvailable
) {
}
