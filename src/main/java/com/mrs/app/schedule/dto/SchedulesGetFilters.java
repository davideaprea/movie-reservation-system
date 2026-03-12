package com.mrs.app.schedule.dto;

import java.time.LocalDateTime;

public record SchedulesGetFilters(
        Long hallId,
        Long movieId,
        LocalDateTime startTimeFrom,
        LocalDateTime endTimeTo
) {
}
