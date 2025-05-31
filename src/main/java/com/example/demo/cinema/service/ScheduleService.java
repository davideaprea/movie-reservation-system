package com.example.demo.cinema.service;

import com.example.demo.cinema.dto.ScheduleDto;
import com.example.demo.cinema.entity.Movie;
import com.example.demo.cinema.entity.Schedule;
import com.example.demo.cinema.projection.BookingSchedule;
import com.example.demo.cinema.projection.ScheduleDate;
import com.example.demo.cinema.repository.ScheduleDao;
import com.example.demo.cinema.response.DaySchedule;
import com.example.demo.cinema.projection.UpcomingSchedule;
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
        Movie movieToSchedule = movieService.findById(dto.movieId());

        LocalDateTime scheduleEndTime = dto
                .startTime()
                .plusMinutes(movieToSchedule.getDuration());

        if (scheduleDao.isHallTaken(
                dto.hallId(),
                dto.startTime(),
                scheduleEndTime
        )) throw new ResponseStatusException(HttpStatus.CONFLICT, "This hall is already taken.");

        return scheduleDao.save(Schedule.create(
                dto.movieId(),
                dto.hallId(),
                dto.startTime(),
                scheduleEndTime
        ));
    }

    public List<ScheduleDate> findUpcomingMovieScheduleDates(long movieId) {
        return scheduleDao.findUpcomingMovieScheduleDates(movieId);
    }

    public List<UpcomingSchedule> findMovieSchedulesByDate(long movieId, LocalDate date) {
        LocalDateTime startOfTheDay = date.atStartOfDay();
        LocalDateTime endOfTheDay = startOfTheDay.plusDays(1);

        return scheduleDao.findMovieSchedulesByDateRange(movieId, startOfTheDay, endOfTheDay);
    }

    public BookingSchedule findBookingScheduleById(long id) {
        return scheduleDao
                .findBookingScheduleById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found."));
    }
}
