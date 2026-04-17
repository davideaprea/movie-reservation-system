package com.mrs.app.movie.service;

import com.mrs.app.movie.dto.MovieCreateRequest;
import com.mrs.app.movie.dto.MovieResponse;
import com.mrs.app.movie.entity.Genre;
import com.mrs.app.movie.entity.Movie;
import com.mrs.app.movie.mapper.MovieMapper;
import com.mrs.app.movie.repository.MovieRepository;
import com.mrs.app.shared.exception.EntityNotFoundException;
import com.mrs.app.shared.exception.EntityNotFoundError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieResponse create(MovieCreateRequest createRequest) {
        Movie movieToSave = movieMapper.toEntity(createRequest);

        createRequest.genreIds().forEach(id -> movieToSave.addGenre(new Genre(id, null)));

        Movie savedMovie = movieRepository.save(movieToSave);

        return movieMapper.toResponse(savedMovie);
    }

    public MovieResponse findById(long id) {
        return movieRepository
                .findById(id)
                .map(movieMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException(new EntityNotFoundError(
                        Movie.class.getSimpleName(),
                        Map.of("id", id)
                )));
    }

    public List<MovieResponse> findAllByTitle(String title) {
        return movieRepository.findByTitle(title)
                .stream()
                .map(movieMapper::toResponse)
                .toList();
    }
}
