package io.github.davideaprea.repository;

import com.mrs.app.hall.entity.Hall;
import org.springframework.data.repository.CrudRepository;

public interface HallDAO extends CrudRepository<Hall, Long> {
}
