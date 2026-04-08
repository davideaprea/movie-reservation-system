package io.github.davideaprea.hall.repository;

import io.github.davideaprea.hall.entity.Seat;
import org.springframework.data.repository.CrudRepository;

public interface SeatDAO extends CrudRepository<Seat, Long> {
}
