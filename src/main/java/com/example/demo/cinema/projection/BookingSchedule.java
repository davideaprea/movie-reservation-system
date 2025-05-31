package com.example.demo.cinema.projection;

import java.time.LocalDateTime;

public record BookingSchedule(
        LocalDateTime startTime,
        long hallId
) {
}
