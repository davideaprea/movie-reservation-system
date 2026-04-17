package com.mrs.app.schedule.service;

import com.mrs.app.schedule.repository.ScheduleSeatRepository;
import com.mrs.app.schedule.dto.ScheduleSeatResponse;
import com.mrs.app.schedule.mapper.ScheduleSeatMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ScheduleSeatService {
    private final ScheduleSeatRepository scheduleSeatRepository;
    private final ScheduleSeatMapper scheduleSeatMapper;

    public List<ScheduleSeatResponse> findAllByIdIn(List<Long> seatIds) {
        return scheduleSeatRepository
                .findAllByIdIn(seatIds)
                .stream()
                .map(scheduleSeatMapper::toResponse)
                .toList();
    }
}
