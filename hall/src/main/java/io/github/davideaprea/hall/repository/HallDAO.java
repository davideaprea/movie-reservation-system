package io.github.davideaprea.hall.repository;

import io.github.davideaprea.hall.entity.Hall;
import org.springframework.data.repository.CrudRepository;

public interface HallDAO extends CrudRepository<Hall, Long> {
}
