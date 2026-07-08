package com.mrs.app.location.mapper;

import com.mrs.app.location.dto.SeatResponse;
import com.mrs.app.location.dto.SeatTypeResponse;
import com.mrs.app.location.entity.Seat;
import com.mrs.app.location.entity.SeatType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    SeatResponse toResponse(Seat seat);

    SeatTypeResponse toResponse(SeatType seatType);
}
