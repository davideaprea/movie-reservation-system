package com.mrs.app.booking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record BookingCreateRequest(
        List<@Valid @Positive Long> seatNumbers,

        @Positive
        long scheduleId,

        @Positive
        long userId,

        @Positive
        long rowNumber
) {
}
