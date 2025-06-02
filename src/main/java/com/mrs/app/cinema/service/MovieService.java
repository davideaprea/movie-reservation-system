package com.mrs.app.cinema.service;

import com.mrs.app.cinema.dto.request.MovieDto;
import com.mrs.app.cinema.entity.Movie;
import com.mrs.app.cinema.repository.MovieDao;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@Service
public class MovieService {
    private final MovieDao movieDao;

    public Movie create(MovieDto dto) {
        return movieDao.save(Movie.create(
                dto.title(),
                dto.duration(),
                dto.description(),
                dto.cover()
        ));
    }

    public Movie findById(long id) {
        return movieDao
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found."));
    }
}
