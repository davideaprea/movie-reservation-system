package com.mrs.app.location.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CinemaAddress(
        @Pattern(regexp = "^[^0-9]*$")
        @NotBlank
        String city,

        @NotBlank
        String zipCode,

        @NotBlank
        String name,

        @NotBlank
        String number
) {
}
