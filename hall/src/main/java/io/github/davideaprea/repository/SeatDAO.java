package io.github.davideaprea.repository;

import com.mrs.app.hall.entity.Seat;
import org.springframework.data.repository.CrudRepository;

public interface SeatDAO extends CrudRepository<Seat, Long> {
}
