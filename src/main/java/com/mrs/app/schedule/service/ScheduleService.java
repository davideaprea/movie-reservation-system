package com.mrs.app.schedule.service;

import com.mrs.app.catalog.dto.MovieDTO;
import com.mrs.app.location.service.SeatService;
import com.mrs.app.schedule.dao.ScheduleSeatDAO;
import com.mrs.app.schedule.dto.ScheduleCreateRequest;
import com.mrs.app.catalog.service.MovieService;
import com.mrs.app.schedule.dto.ScheduleDTO;
import com.mrs.app.schedule.entity.Schedule;
import com.mrs.app.schedule.dao.ScheduleDAO;
import com.mrs.app.schedule.entity.ScheduleSeat;
import com.mrs.app.schedule.mapper.ScheduleMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleDAO scheduleDAO;
    private final ScheduleSeatDAO scheduleSeatDAO;
    private final ScheduleMapper scheduleMapper;
    private final MovieService movieService;
    private final SeatService seatService;

    @Transactional
    public ScheduleDTO create(ScheduleCreateRequest dto) {
        MovieDTO movieToSchedule = movieService.findById(dto.movieId());
        LocalDateTime scheduleEndTime = dto.startTime().plus(movieToSchedule.duration());

        if (!findByFilters().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This location is already taken.");
        }

        Schedule scheduleToSave = scheduleMapper.toEntity(dto, scheduleEndTime);
        Schedule savedSchedule = scheduleDAO.save(scheduleToSave);
        List<ScheduleSeat> seats = seatService
                .findAllByHallId(dto.hallId())
                .stream()
                .map(seat -> new ScheduleSeat(null, seat.id(), scheduleToSave))
                .toList();

        scheduleSeatDAO.saveAll(seats);

        return scheduleMapper.toDTO(savedSchedule);
    }

    public ScheduleDTO findById(long id) {
        return scheduleDAO
                .findById(id)
                .map(scheduleMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found."));
    }

    public List<ScheduleDTO> findByFilters() {
    }
}
