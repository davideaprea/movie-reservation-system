package io.github.davideaprea.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@FieldNameConstants
public record ScheduleCreateRequest(
        @Positive
        long movieId,

        @Positive
        long hallId,

        @NotNull @Future
        LocalDateTime startTime,

        Map<String, BigDecimal> seatPriceOptions
) {
}
