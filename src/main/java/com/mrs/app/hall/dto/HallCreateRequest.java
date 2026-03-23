package com.mrs.app.hall.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record HallCreateRequest(
        @NotBlank
        String name,

        @NotEmpty
        List<@Valid SeatDTO> seats
) {
    public record SeatDTO(
            @Positive
            int rowNumber,

            @Positive
            int seatNumber,

            @Positive
            long seatTypeId
    ) {
    }
}
