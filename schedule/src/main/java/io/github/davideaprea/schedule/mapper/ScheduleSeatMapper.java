package io.github.davideaprea.schedule.mapper;

import io.github.davideaprea.schedule.dto.ScheduleSeatResponse;
import io.github.davideaprea.schedule.entity.ScheduleSeat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScheduleSeatMapper {
    ScheduleSeatResponse toResponse(ScheduleSeat scheduleSeat);
}
