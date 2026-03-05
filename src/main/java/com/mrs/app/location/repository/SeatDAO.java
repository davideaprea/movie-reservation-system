package com.mrs.app.location.repository;

import com.mrs.app.location.entity.Seat;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SeatDAO extends CrudRepository<Seat, Long> {
    List<Seat> findAllByHallId(long hallId);
}
