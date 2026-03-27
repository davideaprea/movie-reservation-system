package com.mrs.app.movie.repository;

import com.mrs.app.movie.entity.Genre;
import org.springframework.data.repository.CrudRepository;

public interface GenreDAO extends CrudRepository<Genre, Long> {
}
