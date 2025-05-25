package com.example.demo.cinema.repository;

import com.example.demo.cinema.projection.ScheduleSeatDetails;
import com.example.demo.cinema.projection.SeatProjection;
import com.example.demo.cinema.entity.Seat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SeatDao extends CrudRepository<Seat, Long> {
    @Query("""
        SELECT new com.example.demo.cinema.projection.SeatProjection(
            s.id, s.type, s.rowNumber, s.seatNumber, s.hall.id
        )
        FROM Seat s
        WHERE s.id IN :seatIds
    """)
    List<SeatProjection> findAll(List<Long> seatIds);

    @Query("""
            SELECT new com.example.demo.cinema.projection.ScheduleSeatDetails(
                s.id,
                s.type,
                s.rowNumber,
                s.seatNumber,
                CASE
                    WHEN b.id IS NULL THEN true ELSE false
                END
            )
            FROM Booking b
            RIGHT JOIN Seat s ON b.seat.id = s.id
            WHERE b.schedule.id = :scheduleId
            """)
    List<ScheduleSeatDetails> findScheduleSeats(long scheduleId);
}
