package com.example.demo.cinema.service;

import com.example.demo.cinema.dto.ScheduleDto;
import com.example.demo.cinema.entity.Movie;
import com.example.demo.cinema.entity.Schedule;
import com.example.demo.cinema.repository.ScheduleDao;
import com.example.demo.cinema.response.DaySchedule;
import com.example.demo.cinema.response.UpcomingSchedule;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleDao scheduleDao;
    private final MovieService movieService;

    public Schedule create(ScheduleDto dto) {
        Movie movie = movieService.getById(dto.movieId());

        LocalDateTime endTime = dto
                .startTime()
                .plusMinutes(movie.getDuration());

        if (scheduleDao.isHallTaken(
                dto.hallId(),
                dto.startTime(),
                endTime
        )) throw new ResponseStatusException(HttpStatus.CONFLICT, "This hall is already taken.");

        return scheduleDao.save(Schedule.create(
                dto.movieId(),
                dto.hallId(),
                dto.startTime(),
                endTime
        ));
    }

    public List<DaySchedule> findUpcomingMovieSchedules(long movieId) {
        List<UpcomingSchedule> schedules = scheduleDao.findUpcomingMovieSchedules(movieId);

        return groupSchedulesByDay(schedules);
    }

    private List<DaySchedule> groupSchedulesByDay(List<UpcomingSchedule> schedules) {
        List<DaySchedule> daySchedules = new ArrayList<>();

        if (schedules == null || schedules.isEmpty()) {
            return daySchedules;
        }

        List<UpcomingSchedule> currList = new ArrayList<>();
        LocalDate currentDay = schedules.getFirst().startTime().toLocalDate();

        for (UpcomingSchedule schedule : schedules) {
            LocalDate scheduleDay = schedule.startTime().toLocalDate();

            if (!scheduleDay.equals(currentDay)) {
                daySchedules.add(new DaySchedule(currentDay, currList));
                currList = new ArrayList<>();
                currentDay = scheduleDay;
            }

            currList.add(schedule);
        }

        if (!currList.isEmpty()) {
            daySchedules.add(new DaySchedule(currentDay, currList));
        }

        return daySchedules;
    }
}
