package com.mrs.app.booking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record BookingCreateRequest(
        @Positive
        long scheduleId,

        @NotEmpty
        List<@Positive @Valid Long> scheduleSeatIds
) {
}
