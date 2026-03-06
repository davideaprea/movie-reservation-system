package com.mrs.app.movie.repository;

import com.mrs.app.movie.entity.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MovieDAO extends CrudRepository<Movie, Long> {
    @Query("""
        SELECT DISTINCT m FROM Movie m
        JOIN m.schedules s
        WHERE s.startTime >= CURRENT_TIMESTAMP
    """)
    List<Movie> findNextScheduledMovies();
}
