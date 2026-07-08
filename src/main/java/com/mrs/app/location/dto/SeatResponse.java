package com.mrs.app.location.dto;

public record SeatResponse(
        long id,
        long hallId,
        long rowNumber,
        long seatNumber,
        SeatTypeResponse type
) {
}
