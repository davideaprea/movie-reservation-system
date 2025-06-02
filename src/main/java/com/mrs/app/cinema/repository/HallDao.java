package com.mrs.app.cinema.repository;

import com.mrs.app.cinema.entity.Hall;
import org.springframework.data.repository.CrudRepository;

public interface HallDao extends CrudRepository<Hall, Long> {
}
