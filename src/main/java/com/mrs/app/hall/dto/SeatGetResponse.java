package com.mrs.app.hall.dto;

import java.math.BigDecimal;

public record SeatGetResponse(
        long id,
        long hallId,
        long rowNumber,
        long seatNumber,
        SeatTypeDTO seatType
) {
    public record SeatTypeDTO(
            long id,
            String name,
            BigDecimal price
    ) {
    }
}
