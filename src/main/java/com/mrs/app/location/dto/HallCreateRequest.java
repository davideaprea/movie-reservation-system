package com.mrs.app.location.dto;

import java.util.List;

public record HallCreateRequest(
        long cinemaId,
        String name,
        List<List<SeatCreateRequest>> seatRows
) {
    public record SeatCreateRequest(
            long seatTypeId
    ) {
    }
}
