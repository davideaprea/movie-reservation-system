package com.example.demo.cinema.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record ScheduleDto(
        @Positive
        long movieId,

        @Positive
        long hallId,

        @Future
        LocalDateTime startTime
) {
}
