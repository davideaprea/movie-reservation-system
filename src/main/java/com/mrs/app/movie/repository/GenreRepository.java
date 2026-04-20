package com.mrs.app.movie.repository;

import com.mrs.app.movie.entity.Genre;
import org.springframework.data.repository.CrudRepository;

public interface GenreRepository extends CrudRepository<Genre, Long> {
}
