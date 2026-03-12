package com.mrs.app.schedule.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.List;

@FieldNameConstants
public record ScheduleCreateRequest(
        @Positive
        long movieId,

        @Positive
        long hallId,

        @NotNull @Future
        LocalDateTime startTime,

        @NotNull @Future
        LocalDateTime endTime,

        @NotNull @Size(min = 1)
        List<@Valid SeatDTO> seats
) {
    public record SeatDTO(
            @Positive
            long seatNumber,

            @Positive
            long rowNumber
    ) {
    }
}
