package io.github.davideaprea.hall.dto;

public record SeatResponse(
        long id,
        long hallId,
        long rowNumber,
        long seatNumber,
        SeatTypeResponse type
) {
}
