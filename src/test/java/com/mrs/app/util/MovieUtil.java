package com.mrs.app.util;

import com.mrs.app.movie.entity.Movie;
import com.mrs.app.movie.repository.MovieDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MovieUtil {
    private final MovieDAO movieDao;

    public Movie createFakeMovie() {
        return movieDao.save(Movie.create(
                "Title",
                110,
                "Description",
                "cover"
        ));
    }
}
