package io.github.davideaprea.schedule.service;

import io.github.davideaprea.schedule.dao.ScheduleSeatDAO;
import io.github.davideaprea.schedule.dto.ScheduleSeatResponse;
import io.github.davideaprea.schedule.mapper.ScheduleSeatMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ScheduleSeatService {
    private final ScheduleSeatDAO scheduleSeatDAO;
    private final ScheduleSeatMapper scheduleSeatMapper;

    public List<ScheduleSeatResponse> findAllByIdIn(List<Long> seatIds) {
        return scheduleSeatDAO
                .findAllByIdIn(seatIds)
                .stream()
                .map(scheduleSeatMapper::toResponse)
                .toList();
    }
}
