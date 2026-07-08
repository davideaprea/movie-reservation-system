package com.mrs.app.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record HTTPAdminCreateRequest(
        @Email
        @NotBlank
        String email,
        @NotBlank
        String password,
        @Positive
        long cinemaId
) {
}
