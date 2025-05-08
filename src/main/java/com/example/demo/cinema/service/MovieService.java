package com.example.demo.cinema.service;

import com.example.demo.cinema.dto.MovieDto;
import com.example.demo.cinema.entity.Movie;
import com.example.demo.cinema.repository.MovieDao;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@Service
public class MovieService {
    private final MovieDao movieDao;

    public Movie create(MovieDto dto) {
        //TODO: save cover on S3

        return movieDao.save(Movie.create(
                dto.title(),
                dto.duration(),
                dto.description(),
                "cover-url"
        ));
    }

    public Movie getById(long id) {
        return movieDao
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found."));
    }
}
