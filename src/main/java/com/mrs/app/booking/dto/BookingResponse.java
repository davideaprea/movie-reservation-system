package com.mrs.app.booking.dto;

import java.util.List;

public record BookingResponse(
        long id,
        long scheduleId,
        List<SeatReservationResponse> seatReservations
) {
    public record SeatReservationResponse(
            long id,
            long scheduleSeatId
    ) {
    }
}
