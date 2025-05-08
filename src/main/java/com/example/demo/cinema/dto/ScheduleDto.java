package com.example.demo.cinema.dto;

import java.time.LocalDateTime;

public record ScheduleDto(
        long movieId,
        long hallId,
        LocalDateTime startTime
) {
}
