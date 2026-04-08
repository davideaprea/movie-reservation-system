package io.github.davideaprea.booking.repository;

import io.github.davideaprea.booking.entity.Booking;
import org.springframework.data.repository.CrudRepository;

public interface BookingDAO extends CrudRepository<Booking, Long> {
}
