package com.example.demo.cinema.projection;

import com.example.demo.cinema.enumeration.SeatType;

public record ScheduleSeatDetails(
        long id,
        SeatType type,
        int rowNumber,
        int seatNumber,
        boolean isAvailable
) {
}
