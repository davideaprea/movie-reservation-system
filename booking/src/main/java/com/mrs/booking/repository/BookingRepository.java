package com.mrs.booking.repository;

import com.mrs.booking.entity.Booking;
import com.mrs.booking.entity.SeatReservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookingRepository extends CrudRepository<Booking, Long> {
    @Query("""
            SELECT sr
            FROM SeatReservation sr
            WHERE sr.booking.scheduleId = :scheduleId
            """)
    List<SeatReservation> findAllByScheduleId(long scheduleId);
}
