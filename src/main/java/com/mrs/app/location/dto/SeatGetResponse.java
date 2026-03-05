package com.mrs.app.location.dto;

import java.math.BigDecimal;

public record SeatGetResponse(
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
