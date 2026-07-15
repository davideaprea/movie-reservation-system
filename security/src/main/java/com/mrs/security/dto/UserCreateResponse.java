package com.mrs.security.dto;

import com.mrs.security.enumeration.Role;

public record UserCreateResponse(
        long id,
        String email,
        Role role,
        Long cinemaId
) {
}
