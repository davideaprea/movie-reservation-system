package com.mrs.app.security.dto;

import com.mrs.app.security.enumeration.Role;

public record UserCreateRequest(
        String email,
        String password,
        Role role,
        Long cinemaId
) {
}
