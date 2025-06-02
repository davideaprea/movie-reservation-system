package com.mrs.app.util;

import com.mrs.app.cinema.entity.Movie;
import com.mrs.app.cinema.repository.MovieDao;
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
