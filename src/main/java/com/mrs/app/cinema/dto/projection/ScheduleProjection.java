package com.mrs.app.cinema.dto.projection;

import java.time.LocalDateTime;

public record ScheduleProjection(
        long id,
        long movieId,
        long hallId,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
