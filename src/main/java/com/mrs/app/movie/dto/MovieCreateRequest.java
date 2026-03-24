package com.mrs.app.movie.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.Duration;
import java.util.List;

public record MovieCreateRequest(
        @NotBlank @Max(100)
        String title,

        @NotNull
        Duration duration,

        @NotBlank @Min(20) @Max(500)
        String description,

        @NotBlank
        String cover,

        @NotEmpty
        List<@Valid @Positive Long> genreIds
) {
}
