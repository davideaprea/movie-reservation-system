package com.mrs.shared.model;

public record LoggedUser(
        long id,
        Role role,
        Long cinemaId
) {
}
