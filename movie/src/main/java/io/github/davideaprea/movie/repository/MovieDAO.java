package io.github.davideaprea.movie.repository;

import io.github.davideaprea.movie.entity.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MovieDAO extends CrudRepository<Movie, Long> {
    List<Movie> findByTitleContainingIgnoreCase(String title);
}
