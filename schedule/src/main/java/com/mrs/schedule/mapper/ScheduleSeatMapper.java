package com.mrs.schedule.mapper;

import com.mrs.schedule.dto.ScheduleSeatResponse;
import com.mrs.schedule.entity.ScheduleSeat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScheduleSeatMapper {
    ScheduleSeatResponse toResponse(ScheduleSeat scheduleSeat);
}
