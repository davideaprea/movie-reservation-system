package com.mrs.app.security.dto;

import com.mrs.app.security.enumeration.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record HTTPOperatorCreateRequest(
        @Email
        @NotBlank
        String email,
        @NotBlank
        String password,
        @Positive
        long cinemaId,
        @NotNull
        Role role
) {
}
