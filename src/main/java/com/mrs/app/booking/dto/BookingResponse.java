package com.mrs.app.booking.dto;

public record BookingResponse(
        long id,
        long scheduleSeatId,
        long userId
) {
}
