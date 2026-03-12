package com.mrs.app.schedule.mapper;

import com.mrs.app.schedule.dto.ScheduleCreateRequest;
import com.mrs.app.schedule.dto.ScheduleResponse;
import com.mrs.app.schedule.entity.Schedule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    Schedule toEntity(ScheduleCreateRequest createRequest);

    ScheduleResponse toDTO(Schedule schedule);
}
