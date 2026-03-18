package com.mrs.app.schedule.service;

import com.mrs.app.movie.dto.MovieGetResponse;
import com.mrs.app.hall.service.HallService;
import com.mrs.app.schedule.dao.ScheduleSpecificationBuilder;
import com.mrs.app.schedule.dto.ScheduleCreateRequest;
import com.mrs.app.movie.service.MovieService;
import com.mrs.app.schedule.dto.ScheduleResponse;
import com.mrs.app.schedule.dto.SchedulesGetFilters;
import com.mrs.app.schedule.entity.Schedule;
import com.mrs.app.schedule.dao.ScheduleDAO;
import com.mrs.app.schedule.entity.ScheduleSeat;
import com.mrs.app.schedule.mapper.ScheduleMapper;
import com.mrs.app.shared.exception.ConflictingEntityException;
import com.mrs.app.shared.exception.ConflictingResourceError;
import com.mrs.app.shared.exception.EntityNotFondException;
import com.mrs.app.shared.exception.EntityNotFoundError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleDAO scheduleDAO;
    private final ScheduleMapper scheduleMapper;
    private final MovieService movieService;
    private final HallService hallService;

    @Transactional
    public ScheduleResponse create(ScheduleCreateRequest dto) {
        MovieGetResponse movieToSchedule = movieService.findById(dto.movieId());
        LocalDateTime scheduleEndTime = dto.startTime().plus(movieToSchedule.duration());
        Schedule scheduleToSave = scheduleMapper.toEntity(dto, scheduleEndTime);
        List<ScheduleResponse> conflictingSchedules = findByFilters(new SchedulesGetFilters(dto.movieId(), dto.startTime(), scheduleEndTime));

        if (!conflictingSchedules.isEmpty()) {
            ConflictingResourceError error = new ConflictingResourceError(
                    conflictingSchedules,
                    List.of(ScheduleCreateRequest.Fields.startTime, ScheduleCreateRequest.Fields.hallId),
                    "This hall is already taken."
            );

            throw new ConflictingEntityException(error);
        }

        hallService
                .findById(dto.hallId())
                .seats()
                .forEach(seat -> {
                    BigDecimal seatPrice = dto.seatPriceOptions().get(seat.seatType().name());

                    scheduleToSave.addSeat(new ScheduleSeat(null, seat.id(), scheduleToSave, seatPrice));
                });

        Schedule schedule = scheduleDAO.save(scheduleToSave);

        return scheduleMapper.toDTO(schedule);
    }

    public ScheduleResponse findById(long id) {
        return scheduleDAO
                .findById(id)
                .map(scheduleMapper::toDTO)
                .orElseThrow(() -> new EntityNotFondException(new EntityNotFoundError(
                        Schedule.class.getSimpleName(),
                        Map.of("id", id)
                )));
    }

    public List<ScheduleResponse> findByFilters(SchedulesGetFilters filters) {
        return scheduleDAO
                .findAll(ScheduleSpecificationBuilder.fromFilters(filters))
                .stream()
                .map(scheduleMapper::toDTO)
                .toList();
    }
}
