package com.mrs.app.booking.dto;

public record BookingResponse(
        long id,
        long scheduleId
) {
    public record SeatReservationResponse(
            long id,
            long scheduleSeatId
    ) {
    }
}
