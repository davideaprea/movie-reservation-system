package com.mrs.app.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record HTTPOrderCreateRequest(
        @Positive
        long scheduleId,

        @NotEmpty
        List<@Positive @Valid Long> seatIds
) {
}
