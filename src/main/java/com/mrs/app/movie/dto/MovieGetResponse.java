package com.mrs.app.movie.dto;

import java.time.Duration;
import java.util.List;

public record MovieGetResponse(
        long id,
        String title,
        Duration duration,
        String description,
        String cover,
        List<GenreDTO> genres
) {
    public record GenreDTO(
            long id,
            String name
    ) {
    }
}
