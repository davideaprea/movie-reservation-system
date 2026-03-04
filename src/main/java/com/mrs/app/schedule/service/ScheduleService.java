package com.mrs.app.schedule.service;

import com.mrs.app.catalog.dto.MovieDTO;
import com.mrs.app.schedule.dto.ScheduleCreateRequest;
import com.mrs.app.catalog.service.MovieService;
import com.mrs.app.schedule.dto.ScheduleDTO;
import com.mrs.app.schedule.entity.Schedule;
import com.mrs.app.schedule.dao.ScheduleDAO;
import com.mrs.app.schedule.mapper.ScheduleMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleDAO scheduleDao;
    private final ScheduleMapper scheduleMapper;
    private final MovieService movieService;

    public ScheduleDTO create(ScheduleCreateRequest dto) {
        MovieDTO movieToSchedule = movieService.findById(dto.movieId());
        LocalDateTime scheduleEndTime = dto.startTime().plus(movieToSchedule.duration());

        if (!findByFilters().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This location is already taken.");
        }

        Schedule scheduleToSave = scheduleMapper.toEntity(dto, scheduleEndTime);
        Schedule savedSchedule = scheduleDao.save(scheduleToSave);

        return scheduleMapper.toDTO(savedSchedule);
    }

    public Schedule findById(long id) {
        return scheduleDao
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found."));
    }

    public List<ScheduleDTO> findByFilters() {

    }
}
