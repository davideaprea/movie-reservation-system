package com.mrs.app.security.dto;

import com.mrs.app.security.enumeration.Role;

public record LoggedUser(
        long id,
        Role role,
        Long cinemaId
) {
}
