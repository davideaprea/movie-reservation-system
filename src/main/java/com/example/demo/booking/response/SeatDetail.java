package com.example.demo.booking.response;

import com.example.demo.cinema.enumeration.SeatType;

public record SeatDetail(
        long id,
        SeatType seatType,
        int rowNumber,
        int seatNumber,
        long hallId
) {
}
