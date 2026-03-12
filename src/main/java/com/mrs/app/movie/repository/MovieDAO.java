package com.mrs.app.movie.repository;

import com.mrs.app.movie.entity.Movie;
import org.springframework.data.repository.CrudRepository;

public interface MovieDAO extends CrudRepository<Movie, Long> {
}
