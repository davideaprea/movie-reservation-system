package com.mrs.app.cinema.mapper;

import com.mrs.app.cinema.dto.projection.ScheduleProjection;
import com.mrs.app.cinema.entity.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ScheduleMapper {
    ScheduleMapper INSTANCE = Mappers.getMapper(ScheduleMapper.class);

    @Mapping(source = "movie.id", target = "movieId")
    @Mapping(source = "hall.id", target = "hallId")
    ScheduleProjection toProjection(Schedule schedule);
}
