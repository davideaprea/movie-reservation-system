package com.mrs.app.schedule.service;

import com.mrs.app.schedule.dao.ScheduleSpecificationBuilder;
import com.mrs.app.schedule.dao.ScheduleDAO;
import com.mrs.app.schedule.dto.ScheduleCreateRequest;
import com.mrs.app.schedule.dto.ScheduleResponse;
import com.mrs.app.schedule.dto.SchedulesGetFilters;
import com.mrs.app.schedule.entity.Schedule;
import com.mrs.app.schedule.mapper.ScheduleMapper;
import com.mrs.app.shared.exception.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleDAO scheduleDAO;
    private final ScheduleMapper scheduleMapper;

    @Transactional
    public ScheduleResponse create(ScheduleCreateRequest dto) {
        if (dto.startTime().isAfter(dto.endTime())) {
            throw new DomainRequirementException(new DomainRequirementError(
                    "Schedule start time must be set before its end time.",
                    ScheduleCreateRequest.Fields.startTime
            ));
        }

        List<ScheduleResponse> conflictingSchedules = findByFilters(new SchedulesGetFilters(dto.hallId(), dto.movieId(), dto.startTime(), dto.endTime()));

        if (!conflictingSchedules.isEmpty()) {
            ConflictingResourceError error = new ConflictingResourceError(
                    conflictingSchedules,
                    List.of(ScheduleCreateRequest.Fields.startTime),
                    "This hall is already taken."
            );

            throw new ConflictingEntityException(error);
        }

        Schedule scheduleToSave = scheduleMapper.toEntity(dto);
        Schedule savedSchedule = scheduleDAO.save(scheduleToSave);

        return scheduleMapper.toDTO(savedSchedule);
    }

    public ScheduleResponse findById(long id) {
        return scheduleDAO.findById(id)
                .map(scheduleMapper::toDTO)
                .orElseThrow(() -> {
                    EntityNotFoundError error = new EntityNotFoundError(
                            Schedule.class.getSimpleName(),
                            Map.of("id", id)
                    );

                    return new EntityNotFondException(error);
                });
    }

    public List<ScheduleResponse> findByFilters(SchedulesGetFilters filters) {
        return scheduleDAO
                .findAll(new ScheduleSpecificationBuilder()
                        .startTimeFrom(filters.startTimeFrom())
                        .endTimeTo(filters.endTimeTo())
                        .movieId(filters.movieId())
                        .build())
                .stream()
                .map(scheduleMapper::toDTO)
                .toList();
    }
}
