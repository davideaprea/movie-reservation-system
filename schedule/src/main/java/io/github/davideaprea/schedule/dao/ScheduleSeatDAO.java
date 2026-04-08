package io.github.davideaprea.schedule.dao;

import com.mrs.app.schedule.entity.ScheduleSeat;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScheduleSeatDAO extends CrudRepository<ScheduleSeat, Long> {
    List<ScheduleSeat> findAllByIdIn(List<Long> seatIds);
}
