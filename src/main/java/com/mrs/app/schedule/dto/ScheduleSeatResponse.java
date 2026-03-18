package com.mrs.app.schedule.dto;

import java.math.BigDecimal;

public record ScheduleSeatResponse(
        long id,
        long seatId,
        BigDecimal price
) {
}
