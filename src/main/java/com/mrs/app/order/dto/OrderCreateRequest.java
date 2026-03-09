package com.mrs.app.order.dto;

import java.util.List;

public record OrderCreateRequest(
        long userId,
        long scheduleId,
        List<Long> seatIds
) {
}
