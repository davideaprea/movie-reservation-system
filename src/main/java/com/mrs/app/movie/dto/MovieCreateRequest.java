package com.mrs.app.movie.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.Duration;
import java.util.List;

public record MovieCreateRequest(
        @NotBlank @Size(max = 100)
        String title,

        @NotNull
        Duration duration,

        @NotBlank @Size(min = 20, max = 500)
        String description,

        @NotBlank
        String coverImageLink,

        @NotEmpty
        List<@Valid @Positive Long> genreIds
) {
}
