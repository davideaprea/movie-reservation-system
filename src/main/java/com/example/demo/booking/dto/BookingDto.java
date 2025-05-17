package com.example.demo.booking.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BookingDto(
        @Size(min = 1, max = 10)
        List<Long> seatIds,

        @Positive
        long scheduleId
) {
}
