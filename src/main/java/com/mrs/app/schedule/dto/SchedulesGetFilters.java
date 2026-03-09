package com.mrs.app.schedule.dto;

import java.time.LocalDateTime;

public record SchedulesGetFilters(
        Long movieId,
        LocalDateTime startTimeFrom,
        LocalDateTime endTimeTo
) {
}
