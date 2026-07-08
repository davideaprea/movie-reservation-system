package com.mrs.app.security.dto;

import com.mrs.app.security.enumeration.Role;

public record UserCreateResponse(
        long id,
        String email,
        Role role,
        Long cinemaId
) {
}
