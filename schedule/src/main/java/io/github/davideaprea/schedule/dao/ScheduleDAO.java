package io.github.davideaprea.schedule.dao;

import io.github.davideaprea.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ScheduleDAO extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {
}
