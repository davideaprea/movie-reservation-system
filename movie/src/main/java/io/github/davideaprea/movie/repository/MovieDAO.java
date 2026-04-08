package io.github.davideaprea.movie.repository;

import com.mrs.app.movie.entity.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MovieDAO extends CrudRepository<Movie, Long> {
    List<Movie> findByTitleContainingIgnoreCase(String title);
}
