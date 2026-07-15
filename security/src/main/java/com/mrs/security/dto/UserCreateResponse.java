package com.mrs.security.dto;

import com.mrs.shared.model.Role;

public record UserCreateResponse(
        long id,
        String email,
        Role role,
        Long cinemaId
) {
}
