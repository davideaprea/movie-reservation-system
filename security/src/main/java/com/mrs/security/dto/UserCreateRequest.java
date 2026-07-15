package com.mrs.security.dto;

import com.mrs.shared.model.Role;

public record UserCreateRequest(
        String email,
        String password,
        Role role,
        Long cinemaId
) {
}
