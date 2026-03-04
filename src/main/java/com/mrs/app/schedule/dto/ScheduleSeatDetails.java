package com.mrs.app.schedule.dto;

public record ScheduleSeatDetails(
        long id,
        SeatType type,
        int rowNumber,
        int seatNumber,
        boolean isAvailable
) {
}
