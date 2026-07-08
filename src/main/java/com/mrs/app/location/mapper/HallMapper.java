package com.mrs.app.location.mapper;

import com.mrs.app.location.dto.HTTPHallCreateRequest;
import com.mrs.app.location.dto.HallCreateRequest;
import com.mrs.app.location.dto.HallGetResponse;
import com.mrs.app.location.dto.HallResponse;
import com.mrs.app.location.entity.Hall;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SeatMapper.class})
public interface HallMapper {
    HallResponse toResponse(Hall hall);

    HallGetResponse toGetResponse(Hall hall);

    HallCreateRequest toRequest(HTTPHallCreateRequest request, long cinemaId);
}
