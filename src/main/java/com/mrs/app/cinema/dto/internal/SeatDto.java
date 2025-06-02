package com.mrs.app.cinema.dto.internal;

import com.mrs.app.cinema.enumeration.SeatType;
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
