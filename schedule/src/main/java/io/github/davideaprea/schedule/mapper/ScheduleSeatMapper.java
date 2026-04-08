package io.github.davideaprea.schedule.mapper;

import com.mrs.app.schedule.dto.ScheduleSeatResponse;
import com.mrs.app.schedule.entity.ScheduleSeat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScheduleSeatMapper {
    ScheduleSeatResponse toResponse(ScheduleSeat scheduleSeat);
}
