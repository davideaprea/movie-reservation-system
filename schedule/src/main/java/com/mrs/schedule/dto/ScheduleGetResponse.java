package com.mrs.schedule.dto;

import java.time.LocalDateTime;

public record ScheduleGetResponse(
        long id,
        long movieId,
        long hallId,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
