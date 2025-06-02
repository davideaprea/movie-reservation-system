package com.mrs.app.booking.repository;

import com.mrs.app.booking.entity.Booking;
import org.springframework.data.repository.CrudRepository;

public interface BookingDao extends CrudRepository<Booking, Long> {
}
