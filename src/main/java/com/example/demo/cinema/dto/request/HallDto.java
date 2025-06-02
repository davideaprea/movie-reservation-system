package com.example.demo.cinema.dto.request;

import com.example.demo.cinema.dto.internal.SeatDto;
import jakarta.validation.constraints.Size;

import java.util.List;

public record HallDto(
        @Size(min = 1, max = 400)
        List<SeatDto> seatDtos
) {
}
