package com.mrs.app.schedule.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SchedulesGetFilters(
        Long movieId,

        @NotNull @FutureOrPresent
        LocalDateTime startTimeFrom,

        @NotNull @FutureOrPresent
        LocalDateTime endTimeTo
) {
}
