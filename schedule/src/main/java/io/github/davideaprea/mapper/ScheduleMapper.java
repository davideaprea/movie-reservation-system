package io.github.davideaprea.mapper;

import com.mrs.app.schedule.dto.ScheduleCreateRequest;
import com.mrs.app.schedule.dto.ScheduleResponse;
import com.mrs.app.schedule.entity.Schedule;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    Schedule toEntity(ScheduleCreateRequest createRequest, LocalDateTime endTime);

    ScheduleResponse toResponse(Schedule schedule);
}
