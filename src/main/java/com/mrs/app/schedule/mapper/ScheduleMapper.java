package com.mrs.app.schedule.mapper;

import com.mrs.app.schedule.dto.ScheduleProjection;
import com.mrs.app.schedule.entity.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ScheduleMapper {
    ScheduleMapper INSTANCE = Mappers.getMapper(ScheduleMapper.class);

    @Mapping(source = "schedule.id", target = "movieId")
    @Mapping(source = "location.id", target = "hallId")
    ScheduleProjection toProjection(Schedule schedule);
}
