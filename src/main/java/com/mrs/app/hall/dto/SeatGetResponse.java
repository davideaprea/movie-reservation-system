package com.mrs.app.hall.dto;

public record SeatGetResponse(
        long id,
        long hallId,
        long rowNumber,
        long seatNumber,
        SeatTypeResponse type
) {
}
