package io.github.davideaprea.dto;

import jakarta.validation.constraints.NotEmpty;

public record SeatTypeCreateRequest(
        @NotEmpty
        String name
) {
}
