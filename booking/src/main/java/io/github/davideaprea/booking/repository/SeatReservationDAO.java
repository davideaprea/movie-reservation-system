package io.github.davideaprea.booking.repository;

import io.github.davideaprea.booking.entity.SeatReservation;
import org.springframework.data.repository.CrudRepository;

public interface SeatReservationDAO extends CrudRepository<SeatReservation, Long> {
}
