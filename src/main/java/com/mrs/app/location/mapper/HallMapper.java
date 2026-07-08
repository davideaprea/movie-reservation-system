package com.mrs.app.location.mapper;

import com.mrs.app.location.dto.HallGetResponse;
import com.mrs.app.location.dto.HallResponse;
import com.mrs.app.location.entity.Hall;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SeatMapper.class})
public interface HallMapper {
    @Mapping(source = "cinema.id", target = "cinemaId")
    HallResponse toResponse(Hall hall);

    HallGetResponse toGetResponse(Hall hall);
}
