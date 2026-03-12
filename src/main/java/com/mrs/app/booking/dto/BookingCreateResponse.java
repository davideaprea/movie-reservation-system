package com.mrs.app.booking.dto;

public record BookingCreateResponse(
        long id,
        long scheduleSeatId,
        long userId
) {
}
