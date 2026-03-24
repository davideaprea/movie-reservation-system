package com.mrs.app.hall.mapper;

import com.mrs.app.hall.dto.SeatResponse;
import com.mrs.app.hall.dto.SeatTypeResponse;
import com.mrs.app.hall.entity.Seat;
import com.mrs.app.hall.entity.SeatType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    SeatResponse toResponse(Seat seat);

    SeatTypeResponse toResponse(SeatType seatType);
}
