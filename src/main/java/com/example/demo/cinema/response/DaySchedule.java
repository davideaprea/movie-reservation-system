package com.example.demo.cinema.response;

import com.example.demo.cinema.projection.UpcomingSchedule;

import java.time.LocalDate;
import java.util.List;

public record DaySchedule(
        LocalDate day,
        List<UpcomingSchedule> schedules
) {
}
