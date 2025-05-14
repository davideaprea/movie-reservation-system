package com.example.demo.cinema.projection;

import java.time.LocalDateTime;

public interface BookingSchedule {
    LocalDateTime getStartTime();
    HallDetails getHall();

    interface HallDetails {
        long getId();
    }
}
