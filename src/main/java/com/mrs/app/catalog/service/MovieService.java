package com.mrs.app.catalog.service;

import com.mrs.app.catalog.dto.MovieCreateRequest;
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
    private final MovieDAO movieDao;
    private final MovieMapper movieMapper;

    public Movie create(MovieCreateRequest createRequest) {
        return movieDao.save(movieMapper.toEntity(createRequest));
    }

    public Movie findById(long id) {
        return movieDao
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found."));
    }
}
