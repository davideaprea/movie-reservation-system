package io.github.davideaprea.schedule.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record ScheduleGetRequestFilters(
        @Positive
        Long movieId,

        @NotNull @FutureOrPresent
        LocalDateTime startTimeFrom,

        @NotNull @FutureOrPresent
        LocalDateTime endTimeTo,

        @Positive
        Long hallId
) {
}
