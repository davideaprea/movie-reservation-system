package com.mrs.app.hall.mapper;

import com.mrs.app.hall.dto.HallCreateRequest;
import com.mrs.app.hall.dto.SeatGetResponse;
import com.mrs.app.hall.entity.Seat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    Seat toEntity(HallCreateRequest.SeatDTO seatDTO);

    SeatGetResponse toResponse(Seat seat);
}
