package com.example.demo.booking.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

public record BookingDto(
        @Size(min = 1)
        List<Long> seatIds
) {
}
