package com.example.demo.cinema.repository;

import com.example.demo.cinema.entity.Schedule;
import com.example.demo.cinema.projection.BookingSchedule;
import com.example.demo.cinema.projection.UpcomingSchedule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleDao extends CrudRepository<Schedule, Long> {
    @Query("""
                SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
                FROM Schedule s
                WHERE s.hall.id = :hallId AND
                :startTime < s.endTime AND
                :endTime > s.startTime
            """)
    boolean isHallTaken(long hallId, LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT new com.example.demo.cinema.projection.UpcomingSchedule(s.id, s.startTime) FROM Schedule s " +
            "WHERE s.movie.id = :movieId AND " +
            "s.startTime > CURRENT_TIMESTAMP " +
            "ORDER BY s.startTime ASC")
    List<UpcomingSchedule> findUpcomingMovieSchedules(@Param("movieId") long movieId);

    @Query("SELECT new com.example.demo.cinema.projection.BookingSchedule(s.startTime, s.hall.id) FROM Schedule s WHERE s.id = :id")
    Optional<BookingSchedule> findBookingScheduleById(long id);
}
