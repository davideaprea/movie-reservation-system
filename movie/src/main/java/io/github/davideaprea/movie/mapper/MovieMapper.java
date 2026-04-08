package io.github.davideaprea.movie.mapper;

import io.github.davideaprea.movie.dto.MovieCreateRequest;
import io.github.davideaprea.movie.dto.MovieResponse;
import io.github.davideaprea.movie.entity.Movie;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    Movie toEntity(MovieCreateRequest createRequest);

    MovieResponse toResponse(Movie movie);
}
