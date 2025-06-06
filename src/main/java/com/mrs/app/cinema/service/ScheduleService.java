package com.mrs.app.cinema.service;

import com.mrs.app.cinema.dto.request.ScheduleDto;
import com.mrs.app.cinema.entity.Movie;
import com.mrs.app.cinema.entity.Schedule;
import com.mrs.app.cinema.dto.projection.BookingSchedule;
import com.mrs.app.cinema.dto.projection.ScheduleDate;
import com.mrs.app.cinema.repository.ScheduleDao;
import com.mrs.app.cinema.dto.projection.UpcomingSchedule;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

        List<Schedule> conflictingSchedules = scheduleDao.findHallSchedulesInDateRange(
                dto.hallId(),
                dto.startTime(),
                scheduleEndTime
        );

        if(!conflictingSchedules.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This hall is already taken.");
        }

        return scheduleDao.save(Schedule.create(
                dto.movieId(),
                dto.hallId(),
                dto.startTime(),
                scheduleEndTime
        ));
    }

    public List<ScheduleDate> findUpcomingMovieScheduleDates(long movieId) {
        return scheduleDao
                .findUpcomingMovieScheduleDates(movieId)
                .stream()
                .map(startTime -> new ScheduleDate(startTime.toLocalDate()))
                .toList();
    }

    public List<UpcomingSchedule> findMovieSchedulesByDate(long movieId, LocalDate date) {
        LocalDateTime startOfTheDay = date.atStartOfDay();
        LocalDateTime endOfTheDay = startOfTheDay.plusDays(1);

        return scheduleDao.findMovieSchedulesInDateRange(movieId, startOfTheDay, endOfTheDay);
    }

    public BookingSchedule findBookingScheduleById(long id) {
        return scheduleDao
                .findBookingScheduleById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found."));
    }
}
