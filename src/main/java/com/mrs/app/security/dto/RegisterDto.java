package com.mrs.app.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterDto (
    @Email
    @NotBlank
    String email,
    @NotBlank
    String password
) {}
