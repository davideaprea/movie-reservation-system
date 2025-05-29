package com.example.demo.util;

import com.example.demo.cinema.entity.Movie;
import com.example.demo.cinema.repository.MovieDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MovieUtil {
    private final MovieDao movieDao;

    public Movie createFakeMovie() {
        return movieDao.save(Movie.create(
                "Title",
                110,
                "Description",
                "cover"
        ));
    }
}
