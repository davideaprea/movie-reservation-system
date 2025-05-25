package com.example.demo.cinema.projection;

import com.example.demo.cinema.enumeration.SeatType;

public record SeatProjection(
        long id,
        SeatType seatType,
        int rowNumber,
        int seatNumber,
        long hallId
) {
}
