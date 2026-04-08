package io.github.davideaprea.hall.mapper;

import io.github.davideaprea.hall.dto.SeatResponse;
import io.github.davideaprea.hall.dto.SeatTypeResponse;
import io.github.davideaprea.hall.entity.Seat;
import io.github.davideaprea.hall.entity.SeatType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    SeatResponse toResponse(Seat seat);

    SeatTypeResponse toResponse(SeatType seatType);
}
