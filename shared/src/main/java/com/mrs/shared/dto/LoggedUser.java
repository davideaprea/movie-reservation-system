package com.mrs.shared.dto;

public record LoggedUser(
        long id,
        String role,
        Long cinemaId
) {
}
