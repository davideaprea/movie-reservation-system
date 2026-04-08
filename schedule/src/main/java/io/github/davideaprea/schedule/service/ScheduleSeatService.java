package io.github.davideaprea.schedule.service;

import com.mrs.app.schedule.dao.ScheduleSeatDAO;
import com.mrs.app.schedule.dto.ScheduleSeatResponse;
import com.mrs.app.schedule.mapper.ScheduleSeatMapper;
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
