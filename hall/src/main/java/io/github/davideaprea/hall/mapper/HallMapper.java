package io.github.davideaprea.hall.mapper;

import io.github.davideaprea.hall.dto.HallGetResponse;
import io.github.davideaprea.hall.dto.HallResponse;
import io.github.davideaprea.hall.entity.Hall;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SeatMapper.class})
public interface HallMapper {
    HallResponse toResponse(Hall hall);

    HallGetResponse toGetResponse(Hall hall);
}
