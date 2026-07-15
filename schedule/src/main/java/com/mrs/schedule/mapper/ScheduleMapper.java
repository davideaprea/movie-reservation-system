package com.mrs.schedule.mapper;

import com.mrs.schedule.dto.ScheduleCreateRequest;
import com.mrs.schedule.dto.ScheduleGetResponse;
import com.mrs.schedule.dto.ScheduleResponse;
import com.mrs.schedule.entity.Schedule;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    Schedule toEntity(ScheduleCreateRequest createRequest, LocalDateTime endTime);

    ScheduleResponse toResponse(Schedule schedule);

    ScheduleGetResponse toGetResponse(Schedule schedule);
}
