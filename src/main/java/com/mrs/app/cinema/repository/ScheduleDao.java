package com.mrs.app.cinema.repository;

import com.mrs.app.cinema.entity.Schedule;
import com.mrs.app.cinema.dto.projection.BookingSchedule;
import com.mrs.app.cinema.dto.projection.UpcomingSchedule;
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
                    minDate >= s.endTime
            """)
    List<Schedule> findHallSchedulesInDateRange(long hallId, LocalDateTime minDate, LocalDateTime maxDate);

    @Query("""
            SELECT new com.mrs.app.cinema.dto.projection.UpcomingSchedule(s.id, s.startTime)
            FROM Schedule s
            WHERE s.movie.id = :movieId AND
                  s.startTime >= :minDate AND
                  s.startTime < :maxDate AND
            ORDER BY s.startTime ASC
            """)
    List<UpcomingSchedule> findMovieSchedulesInDateRange(long movieId, LocalDateTime minDate, LocalDateTime maxDate);

    @Query("""
            SELECT s.startTime
            FROM Schedule s
            WHERE s.movie.id = :movieId AND s.startTime > CURRENT_TIMESTAMP
            ORDER BY s.startTime
            """)
    List<LocalDateTime> findUpcomingMovieScheduleDates(long movieId);

    @Query("""
            SELECT new com.mrs.app.cinema.dto.projection.BookingSchedule(s.startTime, s.hall.id)
            FROM Schedule s
            WHERE s.id = :id
            """)
    Optional<BookingSchedule> findBookingScheduleById(long id);
}
