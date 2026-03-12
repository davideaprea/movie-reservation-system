package com.mrs.app.schedule.dao;

import com.mrs.app.schedule.entity.ScheduleSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleSeatDAO extends JpaRepository<ScheduleSeat, Long> {
    List<ScheduleSeat> findAllByIdIn(List<Long> ids);
}
