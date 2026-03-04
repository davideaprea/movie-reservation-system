package com.mrs.app.schedule.dao;

import com.mrs.app.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleDAO extends JpaRepository<Schedule, Long> {
}
