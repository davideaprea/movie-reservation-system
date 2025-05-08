package com.example.demo.cinema.response;

import java.time.LocalDate;
import java.util.List;

public record DaySchedule(
        LocalDate day,
        List<UpcomingSchedule> schedules
) {
}
