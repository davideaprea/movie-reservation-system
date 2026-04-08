package io.github.davideaprea.schedule.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ScheduleResponse(
        long id,
        long movieId,
        long hallId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        List<ScheduleSeatResponse> seats
) {
}
