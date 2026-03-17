package com.mrs.app.booking.dto;

public record BookingResponse(
        long id,
        long scheduleId,
        long userId
) {
    public record SeatReservationResponse(
            long id,
            long scheduleSeatId
    ) {
    }
}
