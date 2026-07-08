package com.mrs.app.location.dto;

import jakarta.validation.constraints.NotEmpty;

public record SeatTypeCreateRequest(
        @NotEmpty
        String name
) {
}
