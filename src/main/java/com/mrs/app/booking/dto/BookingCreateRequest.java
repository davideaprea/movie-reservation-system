package com.mrs.app.booking.dto;

import jakarta.validation.constraints.Positive;

public record BookingCreateRequest(
        @Positive
        long orderId,

        @Positive
        long scheduleSeatId
) {
}
