package io.github.davideaprea.hall.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record HallCreateRequest(
        @NotBlank
        String name,

        @NotEmpty
        List<@NotEmpty @Valid List<@NotNull @Valid SeatCreateRequest>> seatRows
) {
    public record SeatCreateRequest(
            @Positive
            long seatTypeId
    ) {
    }
}
