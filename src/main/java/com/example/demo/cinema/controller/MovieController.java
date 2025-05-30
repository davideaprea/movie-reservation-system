package com.example.demo.cinema.controller;

import com.example.demo.cinema.dto.MovieDto;
import com.example.demo.cinema.entity.Movie;
import com.example.demo.cinema.response.DaySchedule;
import com.example.demo.cinema.service.MovieService;
import com.example.demo.cinema.service.ScheduleService;
import com.example.demo.core.enumeration.Routes;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(Routes.MOVIES)
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

    @GetMapping("/{id}" + Routes.SCHEDULES)
    public ResponseEntity<List<DaySchedule>> findUpcomingMovieSchedules(@PathVariable long id) {
        return new ResponseEntity<>(
                scheduleService.findUpcomingMovieSchedules(id),
                HttpStatus.OK
        );
    }
}
