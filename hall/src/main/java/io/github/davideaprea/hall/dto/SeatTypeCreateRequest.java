package io.github.davideaprea.hall.dto;

import jakarta.validation.constraints.NotEmpty;

public record SeatTypeCreateRequest(
        @NotEmpty
        String name
) {
}
