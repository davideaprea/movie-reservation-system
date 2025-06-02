package com.example.demo.cinema.dto.projection;

import com.example.demo.cinema.enumeration.SeatType;

public record SeatProjection(
        long id,
        SeatType type,
        int rowNumber,
        int seatNumber,
        long hallId
) {
}
