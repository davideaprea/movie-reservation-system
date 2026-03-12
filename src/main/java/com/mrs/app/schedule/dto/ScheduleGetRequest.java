package com.mrs.app.schedule.dto;

import java.util.List;

public record ScheduleGetRequest(
        long id,
        List<Long> seatIds
) {
}
