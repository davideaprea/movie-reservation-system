package io.github.davideaprea.schedule.dao;

import io.github.davideaprea.schedule.entity.ScheduleSeat;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScheduleSeatDAO extends CrudRepository<ScheduleSeat, Long> {
    List<ScheduleSeat> findAllByIdIn(List<Long> seatIds);
}
