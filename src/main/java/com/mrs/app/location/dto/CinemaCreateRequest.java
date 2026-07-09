package com.mrs.app.location.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CinemaCreateRequest(
        @NotBlank
        String name,

        @NotNull
        CinemaAddress address
) {
}
