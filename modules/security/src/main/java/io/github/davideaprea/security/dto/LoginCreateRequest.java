package io.github.davideaprea.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginCreateRequest(
    @Email
    @NotBlank
    String email,
    @NotBlank
    String password
) {}
