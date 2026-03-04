package com.mrs.app.schedule.dto;

import com.mrs.app.location.enumeration.SeatType;

public record ScheduleSeatDetails(
        long id,
        SeatType type,
        int rowNumber,
        int seatNumber,
        boolean isAvailable
) {
}
