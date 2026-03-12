package com.mrs.app.schedule.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ScheduleResponse(
        long id,
        long movieId,
        long hallId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        List<SeatDTO> seats
) {
    public record SeatDTO(
            long id,
            long seatId,
            BigDecimal price
    ){}
}
