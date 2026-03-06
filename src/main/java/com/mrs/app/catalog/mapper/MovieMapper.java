package com.mrs.app.catalog.mapper;

import com.mrs.app.catalog.dto.MovieCreateRequest;
import com.mrs.app.catalog.dto.MovieGetResponse;
import com.mrs.app.catalog.entity.Movie;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    Movie toEntity(MovieCreateRequest createRequest);

    MovieGetResponse toDTO(Movie movie);
}
