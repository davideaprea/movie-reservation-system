package com.example.demo.util;

import com.example.demo.cinema.entity.Schedule;
import com.example.demo.cinema.repository.ScheduleDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
@AllArgsConstructor
public class ScheduleUtil {
    private final ScheduleDao scheduleDao;

    public Schedule createFakeSchedule(long movieId, long hallId) {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(2);

        return scheduleDao.save(Schedule.create(
                movieId,
                hallId,
                startTime,
                endTime
        ));
    }

    public List<Schedule> createFakeSchedules(
            int schedulesNumber,
            long movieId,
            long hallId
    ) {
        final LocalDateTime startTime = LocalDateTime.now().plusHours(1);

        List<Schedule> schedules = new ArrayList<>();

        for (int i = (schedulesNumber * -1); i < schedulesNumber; i++) {
            final LocalDateTime date = startTime.plusDays(i);

            schedules.add(Schedule.create(
                    movieId,
                    hallId,
                    date,
                    date.plusHours(2)
            ));
        }

        return StreamSupport
                .stream(scheduleDao.saveAll(schedules).spliterator(), false)
                .toList();
    }

    public List<Schedule> createSchedulesOnDay(long movieId, long hallId, LocalDate day, int schedulesNumber) {
        List<Schedule> schedules = new ArrayList<>();
        LocalDateTime startTime = day.atStartOfDay();

        for (int i = 0; i < schedulesNumber; i++) {
            schedules.add(Schedule.create(
                    movieId,
                    hallId,
                    startTime,
                    startTime.plusHours(2)
            ));

            startTime = startTime.plusHours(3);
        }

        return StreamSupport
                .stream(scheduleDao.saveAll(schedules).spliterator(), false)
                .toList();
    }
}
