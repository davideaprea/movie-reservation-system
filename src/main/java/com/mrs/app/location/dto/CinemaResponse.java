package com.mrs.app.location.dto;

public record CinemaResponse(
        long id,
        String name,
        CinemaAddress address
) {
}
