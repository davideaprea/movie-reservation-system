package com.mrs.app.catalog.service;

import com.mrs.app.catalog.dto.MovieCreateRequest;
import com.mrs.app.catalog.dto.MovieGetResponse;
import com.mrs.app.catalog.entity.Movie;
import com.mrs.app.catalog.mapper.MovieMapper;
import com.mrs.app.catalog.repository.MovieDAO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@Service
public class MovieService {
    private final MovieDAO movieDAO;
    private final MovieMapper movieMapper;

    public MovieGetResponse create(MovieCreateRequest createRequest) {
        Movie movieToSave = movieMapper.toEntity(createRequest);
        Movie savedMovie = movieDAO.save(movieToSave);

        return movieMapper.toDTO(savedMovie);
    }

    public MovieGetResponse findById(long id) {
        return movieDAO
                .findById(id)
                .map(movieMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found."));
    }
}
