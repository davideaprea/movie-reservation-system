package com.mrs.app.cinema.repository;

import com.mrs.app.cinema.dto.projection.ScheduleSeatDetails;
import com.mrs.app.cinema.dto.projection.SeatProjection;
import com.mrs.app.cinema.entity.Seat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SeatDao extends CrudRepository<Seat, Long> {
    @Query("""
        SELECT new com.mrs.app.cinema.dto.projection.SeatProjection(
            s.id, s.type, s.rowNumber, s.seatNumber, s.hall.id
        )
        FROM Seat s
        WHERE s.id IN :seatIds
    """)
    List<SeatProjection> findAll(List<Long> seatIds);

    @Query("""
            SELECT new com.mrs.app.cinema.dto.projection.ScheduleSeatDetails(
                s.id,
                s.type,
                s.rowNumber,
                s.seatNumber,
                CASE
                    WHEN b.id IS NULL THEN true ELSE false
                END
            )
            FROM Seat s
            LEFT JOIN Booking b ON b.seat.id = s.id AND b.schedule.id = :scheduleId
            """)
    List<ScheduleSeatDetails> findScheduleSeats(long scheduleId);
}
