package io.github.davideaprea.movie.service;

import io.github.davideaprea.movie.dto.MovieCreateRequest;
import io.github.davideaprea.movie.dto.MovieResponse;
import io.github.davideaprea.movie.entity.Genre;
import io.github.davideaprea.movie.entity.Movie;
import io.github.davideaprea.movie.mapper.MovieMapper;
import io.github.davideaprea.movie.repository.MovieDAO;
import io.github.davideaprea.shared.exception.EntityNotFondException;
import io.github.davideaprea.shared.exception.EntityNotFoundError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class MovieService {
    private final MovieDAO movieDAO;
    private final MovieMapper movieMapper;

    public MovieResponse create(MovieCreateRequest createRequest) {
        Movie movieToSave = movieMapper.toEntity(createRequest);

        createRequest.genreIds().forEach(id -> movieToSave.addGenre(new Genre(id, null)));

        Movie savedMovie = movieDAO.save(movieToSave);

        return movieMapper.toResponse(savedMovie);
    }

    public MovieResponse findById(long id) {
        return movieDAO
                .findById(id)
                .map(movieMapper::toResponse)
                .orElseThrow(() -> new EntityNotFondException(new EntityNotFoundError(
                        Movie.class.getSimpleName(),
                        Map.of("id", id)
                )));
    }

    public List<MovieResponse> findAllByTitle(String title) {
        return movieDAO.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(movieMapper::toResponse)
                .toList();
    }
}
