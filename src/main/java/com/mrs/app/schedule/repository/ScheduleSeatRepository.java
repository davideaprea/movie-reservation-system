package com.mrs.app.schedule.repository;

import com.mrs.app.schedule.entity.ScheduleSeat;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScheduleSeatRepository extends CrudRepository<ScheduleSeat, Long> {
    List<ScheduleSeat> findAllByIdIn(List<Long> seatIds);
}
