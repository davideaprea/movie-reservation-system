package com.mrs.movie.mapper;

import com.mrs.movie.dto.MovieCreateRequest;
import com.mrs.movie.dto.MovieResponse;
import com.mrs.movie.entity.Movie;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    Movie toEntity(MovieCreateRequest createRequest);

    MovieResponse toResponse(Movie movie);
}
