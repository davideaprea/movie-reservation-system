package com.mrs.app.hall.dto;

import com.mrs.app.hall.enumeration.HallStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record HallCreateRequest(
        @NotNull
        HallStatus status,

        @Size(min = 1, max = 400)
        List<SeatDTO> seats
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
