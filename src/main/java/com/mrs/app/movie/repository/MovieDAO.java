package com.mrs.app.movie.repository;

import com.mrs.app.movie.entity.Movie;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MovieDAO extends CrudRepository<Movie, Long> {
    @EntityGraph(attributePaths = "genres")
    @Query("""
            SELECT m
            FROM Movie m
            WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))
            """)
    List<Movie> findByTitle(String title);

    @EntityGraph(attributePaths = "genres")
    Optional<Movie> findById(Long id);
}
