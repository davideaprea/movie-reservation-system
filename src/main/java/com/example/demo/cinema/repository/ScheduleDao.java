package com.example.demo.cinema.repository;

import com.example.demo.cinema.entity.Schedule;
import org.springframework.data.repository.CrudRepository;

public interface ScheduleDao extends CrudRepository<Schedule, Long> {
}
