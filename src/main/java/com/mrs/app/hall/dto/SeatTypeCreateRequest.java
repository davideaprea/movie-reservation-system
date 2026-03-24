package com.mrs.app.hall.dto;

import jakarta.validation.constraints.NotEmpty;

public record SeatTypeCreateRequest(
        @NotEmpty
        String name
) {
}
