package com.mrs.app.schedule.service;

import com.mrs.app.movie.dto.MovieGetResponse;
import com.mrs.app.hall.service.HallService;
import com.mrs.app.schedule.dao.ScheduleSeatDAO;
import com.mrs.app.schedule.dto.ScheduleCreateRequest;
import com.mrs.app.movie.service.MovieService;
import com.mrs.app.schedule.dto.ScheduleGetResponse;
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
    private final HallService hallService;

    @Transactional
    public ScheduleGetResponse create(ScheduleCreateRequest dto) {
        MovieGetResponse movieToSchedule = movieService.findById(dto.movieId());
        LocalDateTime scheduleEndTime = dto.startTime().plus(movieToSchedule.duration());

        if (!findByFilters().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This hall is already taken.");
        }

        Schedule schedule = scheduleDAO.save(scheduleMapper.toEntity(dto, scheduleEndTime));
        List<ScheduleSeat> seats = hallService
                .findById(dto.hallId())
                .seats()
                .stream()
                .map(seat -> new ScheduleSeat(null, seat.id(), schedule))
                .toList();

        scheduleSeatDAO.saveAll(seats);

        return scheduleMapper.toDTO(schedule);
    }

    public ScheduleGetResponse findById(long id) {
        return scheduleDAO
                .findById(id)
                .map(scheduleMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found."));
    }

    public List<ScheduleGetResponse> findByFilters() {
    }
}
