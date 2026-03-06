package com.mrs.app.movie.mapper;

import com.mrs.app.movie.dto.MovieCreateRequest;
import com.mrs.app.movie.dto.MovieGetResponse;
import com.mrs.app.movie.entity.Movie;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    Movie toEntity(MovieCreateRequest createRequest);

    MovieGetResponse toDTO(Movie movie);
}
