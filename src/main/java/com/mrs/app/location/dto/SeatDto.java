package com.mrs.app.location.dto;

import com.mrs.app.location.enumeration.SeatType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SeatDto(
        @Positive
        int rowNumber,

        @Positive
        int seatNumber,

        @NotNull
        SeatType type
) {
}
