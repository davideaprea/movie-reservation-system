package com.mrs.app.booking.repository;

import com.mrs.app.booking.entity.SeatReservation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SeatReservationRepository extends CrudRepository<SeatReservation, Long> {
    List<SeatReservation> findAllByBookingId(long bookingId);
}
