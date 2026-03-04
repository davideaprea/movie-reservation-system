package com.mrs.app.catalog.dto;

import jakarta.validation.constraints.*;

import java.time.Duration;
import java.util.List;

public record MovieCreateRequest(
        @NotBlank
        String title,

        @NotNull
        Duration duration,

        @NotBlank @Min(20) @Max(500)
        String description,

        @NotBlank
        String cover,

        @Size(min = 1)
        List<Long> genreIds
) {
}
