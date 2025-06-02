package com.mrs.app.cinema.dto.projection;

import java.time.LocalDateTime;

public record BookingSchedule(
        LocalDateTime startTime,
        long hallId
) {
}
