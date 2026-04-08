package io.github.davideaprea.dto;

import lombok.experimental.FieldNameConstants;

import java.util.List;

@FieldNameConstants
public record OrderCreateRequest(
        long userId,
        long scheduleId,
        List<Long> seatIds
) {
}
