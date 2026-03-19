package com.mrs.app.schedule.dao;

import com.mrs.app.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScheduleDAO extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {
    @Query("""
                SELECT DISTINCT sc
                FROM Schedule sc
                JOIN FETCH sc.seats se
                WHERE sc.id = :scheduleId
                AND se.seatId IN :seatIds
            """)
    Optional<Schedule> findByIdWithSeats(long id, List<Long> seatIds);

    long deleteById(long id);
}
