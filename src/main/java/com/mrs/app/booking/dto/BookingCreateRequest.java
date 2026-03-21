package com.mrs.app.booking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@FieldNameConstants
public record BookingCreateRequest(
        @Positive
        long scheduleId,

        @NotEmpty
        List<@Positive @Valid Long> scheduleSeatIds
) {
}
