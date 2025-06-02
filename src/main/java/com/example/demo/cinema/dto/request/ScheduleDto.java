package com.example.demo.cinema.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record ScheduleDto(
        @Positive
        long movieId,

        @Positive
        long hallId,

        @NotNull @Future
        LocalDateTime startTime
) {
}
