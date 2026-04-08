package io.github.davideaprea.movie.dto;

import java.time.Duration;
import java.util.List;

public record MovieResponse(
        long id,
        String title,
        Duration duration,
        String description,
        String cover,
        List<GenreResponse> genres
) {
}
