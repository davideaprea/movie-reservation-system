package com.example.demo.cinema.projection;

import java.time.LocalDateTime;

public record UpcomingSchedule(
        long id,
        LocalDateTime startTime
) {
}
