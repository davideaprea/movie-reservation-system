package com.mrs.app.movie.service;

import com.mrs.app.movie.dto.MovieCreateRequest;
import com.mrs.app.movie.dto.MovieGetResponse;
import com.mrs.app.movie.entity.Movie;
import com.mrs.app.movie.mapper.MovieMapper;
import com.mrs.app.movie.repository.MovieDAO;
import com.mrs.app.shared.exception.EntityNotFondException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new EntityNotFondException("movie", id));
    }
}
