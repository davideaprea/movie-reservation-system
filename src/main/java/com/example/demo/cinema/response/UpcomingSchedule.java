package com.example.demo.cinema.response;

import java.time.LocalDateTime;

public record UpcomingSchedule(
        long id,
        LocalDateTime time
) {
}
