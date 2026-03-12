package com.mrs.app.schedule.service;

import com.mrs.app.schedule.dao.ScheduleSeatDAO;
import com.mrs.app.schedule.dto.ScheduleSeatResponse;
import com.mrs.app.schedule.entity.ScheduleSeat;
import com.mrs.app.schedule.mapper.ScheduleSeatMapper;
import com.mrs.app.shared.exception.EntityNotFondException;
import com.mrs.app.shared.exception.EntityNotFoundError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleSeatService {
    private final ScheduleSeatDAO scheduleSeatDAO;
    private final ScheduleSeatMapper scheduleSeatMapper;

    public List<ScheduleSeatResponse> findAllByIds(List<Long> seatIds) {
        List<ScheduleSeatResponse> seats = scheduleSeatDAO
                .findAllByIdIn(seatIds)
                .stream()
                .map(scheduleSeatMapper::toResponse)
                .toList();

        if (seats.size() != seatIds.size()) {
            throw new EntityNotFondException(new EntityNotFoundError(ScheduleSeat.class.getSimpleName(), seatIds));
        }

        return seats;
    }
}
