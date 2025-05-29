package com.example.demo.cinema.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

public record HallDto(
        @Size(min = 1, max = 400)
        List<SeatDto> seatDtos
) {
}
