package io.github.davideaprea.schedule.mapper;

import io.github.davideaprea.schedule.dto.ScheduleCreateRequest;
import io.github.davideaprea.schedule.dto.ScheduleResponse;
import io.github.davideaprea.schedule.entity.Schedule;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    Schedule toEntity(ScheduleCreateRequest createRequest, LocalDateTime endTime);

    ScheduleResponse toResponse(Schedule schedule);
}
