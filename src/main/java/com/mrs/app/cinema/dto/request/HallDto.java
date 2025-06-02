package com.mrs.app.cinema.dto.request;

import com.mrs.app.cinema.dto.internal.SeatDto;
import jakarta.validation.constraints.Size;

import java.util.List;

public record HallDto(
        @Size(min = 1, max = 400)
        List<SeatDto> seatDtos
) {
}
