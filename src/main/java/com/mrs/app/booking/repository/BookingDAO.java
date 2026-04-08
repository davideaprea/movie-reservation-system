package com.mrs.app.booking.repository;

import com.mrs.app.booking.entity.Booking;
import com.mrs.app.booking.entity.SeatReservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookingDAO extends CrudRepository<Booking, Long> {
    @Query("""
            SELECT sr
            FROM SeatReservation sr
            WHERE sr.booking.scheduleId = :scheduleId
            """)
    List<SeatReservation> findAllByScheduleId(long scheduleId);
}
