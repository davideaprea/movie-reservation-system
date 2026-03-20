package com.mrs.app.booking.repository;

import com.mrs.app.booking.entity.SeatReservation;
import org.springframework.data.repository.CrudRepository;

public interface SeatReservationDAO extends CrudRepository<SeatReservation, Long> {
}
