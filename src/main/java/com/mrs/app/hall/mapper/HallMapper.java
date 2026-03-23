package com.mrs.app.hall.mapper;

import com.mrs.app.hall.dto.HallCreateRequest;
import com.mrs.app.hall.dto.HallResponse;
import com.mrs.app.hall.entity.Hall;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SeatMapper.class})
public interface HallMapper {
    Hall toEntity(HallCreateRequest createRequest);

    HallResponse toResponse(Hall hall);
}
