package com.mrs.app.payment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BookingsPaymentDto(
        @NotNull @Size(min = 1, max = 10)
        List<Long> seatIds,

        @Positive
        long scheduleId
) {
}
