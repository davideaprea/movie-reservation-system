package com.mrs.security.dto;

import com.mrs.security.enumeration.Role;

public record UserCreateRequest(
        String email,
        String password,
        Role role,
        Long cinemaId
) {
}
