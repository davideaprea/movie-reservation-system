package io.github.davideaprea.repository;

import com.mrs.app.booking.entity.Booking;
import org.springframework.data.repository.CrudRepository;

public interface BookingDAO extends CrudRepository<Booking, Long> {
}
