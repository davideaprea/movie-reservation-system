package io.github.davideaprea.movie.repository;

import com.mrs.app.movie.entity.Genre;
import org.springframework.data.repository.CrudRepository;

public interface GenreDAO extends CrudRepository<Genre, Long> {
}
