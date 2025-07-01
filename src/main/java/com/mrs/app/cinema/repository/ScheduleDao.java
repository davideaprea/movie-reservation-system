package com.mrs.app.cinema.repository;

import com.mrs.app.cinema.entity.Schedule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleDao extends CrudRepository<Schedule, Long> {
    @Query("""
                SELECT s
                FROM Schedule s
                WHERE s.hall.id = :hallId AND
                    s.startTime <= :maxDate AND
                    s.endTime >= :minDate
            """)
    List<Schedule> findHallSchedulesInDateRange(long hallId, LocalDateTime minDate, LocalDateTime maxDate);

    @Query("""
            SELECT s
            FROM Schedule s
            WHERE s.movie.id = :movieId AND
                  s.startTime >= :minDate AND
                  s.startTime < :maxDate
            ORDER BY s.startTime ASC
            """)
    List<Schedule> findMovieSchedulesInDateRange(long movieId, LocalDateTime minDate, LocalDateTime maxDate);

    @Query("""
            SELECT s.startTime
            FROM Schedule s
            WHERE s.movie.id = :movieId AND s.startTime > CURRENT_TIMESTAMP
            ORDER BY s.startTime
            """)
    List<LocalDateTime> findUpcomingMovieScheduleDates(long movieId);

    @Query("""
            SELECT DISTINCT b.schedule
            FROM Booking b
            WHERE b.payment.id = :paymentId
            """)
    Optional<Schedule> findPaymentSchedule(long paymentId);
}
