package com.mrs.location.mapper;

import com.mrs.location.dto.HallGetResponse;
import com.mrs.location.dto.HallResponse;
import com.mrs.location.entity.Hall;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SeatMapper.class})
public interface HallMapper {
    @Mapping(source = "cinema.id", target = "cinemaId")
    HallResponse toResponse(Hall hall);

    HallGetResponse toGetResponse(Hall hall);
}
