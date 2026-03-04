package com.mrs.app.location.mapper;

import com.mrs.app.location.dto.HallCreateRequest;
import com.mrs.app.location.entity.Seat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    Seat toEntity(HallCreateRequest.SeatDTO seatDTO);
}
