package com.mrs.app.schedule.mapper;

import com.mrs.app.schedule.dto.ScheduleCreateRequest;
import com.mrs.app.schedule.dto.ScheduleDTO;
import com.mrs.app.schedule.entity.Schedule;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    Schedule toEntity(ScheduleCreateRequest createRequest, LocalDateTime endTime);

    ScheduleDTO toDTO(Schedule schedule);
}
