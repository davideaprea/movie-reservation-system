package com.mrs.app.booking.repository;

import com.mrs.app.booking.entity.Booking;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookingDAO extends CrudRepository<Booking, Long> {
    List<Booking> findAllByScheduleId(long scheduleId);
}
