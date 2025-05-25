package com.example.demo.cinema.repository;

import com.example.demo.cinema.entity.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MovieDao extends CrudRepository<Movie, Long> {
    @Query("""
        SELECT DISTINCT m FROM Movie m
        JOIN m.schedules s
        WHERE s.startTime >= CURRENT_TIMESTAMP
    """)
    List<Movie> getNextScheduledMovies();
}
