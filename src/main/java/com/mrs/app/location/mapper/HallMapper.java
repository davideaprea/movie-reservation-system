package com.mrs.app.location.mapper;

import com.mrs.app.location.dto.HTTPHallCreateRequest;
import com.mrs.app.location.dto.HallCreateRequest;
import com.mrs.app.location.dto.HallGetResponse;
import com.mrs.app.location.dto.HallResponse;
import com.mrs.app.location.entity.Hall;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SeatMapper.class})
public interface HallMapper {
    @Mapping(source = "cinema.id", target = "cinemaId")
    HallResponse toResponse(Hall hall);

    HallGetResponse toGetResponse(Hall hall);

    HallCreateRequest toRequest(HTTPHallCreateRequest request, long cinemaId);

    List<HallCreateRequest.SeatCreateRequest> toRequest(List<HTTPHallCreateRequest.SeatCreateRequest> request);
}
