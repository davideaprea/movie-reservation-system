package com.mrs.app.booking.dto;

public record BookingCreateResponse(
        long id,
        long seatId,
        long scheduleId,
        long userId
) {
}
