package com.mrs.app.location.repository;

import com.mrs.app.location.entity.Hall;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HallRepository extends CrudRepository<Hall, Long> {
    List<Hall> findAllByCinemaId(long cinemaId);
}
