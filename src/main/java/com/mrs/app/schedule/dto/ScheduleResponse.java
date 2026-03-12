package com.mrs.app.schedule.dto;

import java.time.LocalDateTime;

public record ScheduleResponse(
        long id,
        long movieId,
        long hallId,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
