package com.mrs.app.hall.repository;

import com.mrs.app.hall.entity.Hall;
import org.springframework.data.repository.CrudRepository;

public interface HallDAO extends CrudRepository<Hall, Long> {
}
