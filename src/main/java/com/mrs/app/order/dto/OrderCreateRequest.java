package com.mrs.app.order.dto;

import lombok.experimental.FieldNameConstants;

import java.util.List;

@FieldNameConstants
public record OrderCreateRequest(
        long userId,
        long scheduleId,
        List<Long> seatIds
) {
}
