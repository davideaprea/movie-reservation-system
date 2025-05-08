package com.example.demo.cinema.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record MovieDto(
        @NotBlank
        String title,

        @Min(1)
        short duration,

        @Min(20) @Max(500)
        String description,

        @NotNull
        MultipartFile cover
) {
}
