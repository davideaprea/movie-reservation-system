package com.example.demo.cinema.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record MovieDto(
        @NotBlank
        String title,

        @Positive
        short duration,

        @NotBlank @Min(20) @Max(500)
        String description,

        @NotBlank
        String cover
) {
}
