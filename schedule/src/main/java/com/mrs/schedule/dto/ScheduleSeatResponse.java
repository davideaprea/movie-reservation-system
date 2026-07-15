package com.mrs.schedule.dto;

import java.math.BigDecimal;

public record ScheduleSeatResponse(
        long id,
        long seatId,
        BigDecimal price
) {
}
