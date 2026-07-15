package com.mrs.location.mapper;

import com.mrs.location.dto.SeatResponse;
import com.mrs.location.dto.SeatTypeResponse;
import com.mrs.location.entity.Seat;
import com.mrs.location.entity.SeatType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    SeatResponse toResponse(Seat seat);

    SeatTypeResponse toResponse(SeatType seatType);
}
