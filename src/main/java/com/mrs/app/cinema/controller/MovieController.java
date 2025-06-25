package com.mrs.app.cinema.controller;

import com.mrs.app.cinema.dto.projection.ScheduleProjection;
import com.mrs.app.cinema.dto.request.MovieDto;
import com.mrs.app.cinema.entity.Movie;
import com.mrs.app.cinema.dto.response.ScheduleDate;
import com.mrs.app.cinema.entity.Schedule;
import com.mrs.app.cinema.mapper.ScheduleMapper;
import com.mrs.app.cinema.service.MovieService;
import com.mrs.app.cinema.service.ScheduleService;
import com.mrs.app.routes.ControllerRoutes;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(ControllerRoutes.MOVIES)
public class MovieController {
    private final MovieService movieService;
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<Movie> create(@Valid @RequestBody MovieDto dto) {
        return new ResponseEntity<>(
                movieService.create(dto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> findById(@PathVariable long id) {
        return new ResponseEntity<>(
                movieService.findById(id),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}" + ControllerRoutes.SCHEDULES_DATES)
    public ResponseEntity<List<ScheduleDate>> findUpcomingMovieScheduleDates(@PathVariable long id) {
        return new ResponseEntity<>(
                scheduleService.findUpcomingMovieScheduleDates(id),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}" + ControllerRoutes.SCHEDULES_DATES + "/{date}")
    public ResponseEntity<List<ScheduleProjection>> findMovieSchedulesByDate(
            @PathVariable long id,
            @PathVariable @Valid @FutureOrPresent LocalDate date
    ) {
        List<Schedule> schedulesOfTheDay = scheduleService.findMovieSchedulesByDate(id, date);

        List<ScheduleProjection> scheduleProjections = schedulesOfTheDay
                .stream()
                .map(ScheduleMapper.INSTANCE::toProjection)
                .toList();

        return new ResponseEntity<>(
                scheduleProjections,
                HttpStatus.OK
        );
    }
}
