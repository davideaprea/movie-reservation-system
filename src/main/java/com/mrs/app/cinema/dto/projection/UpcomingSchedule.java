package com.mrs.app.cinema.dto.projection;

import java.time.LocalDateTime;

public record UpcomingSchedule(
        long id,
        LocalDateTime startTime
) {
}
