package com.example.demo.cinema.controller;

import com.example.demo.cinema.dto.ScheduleDto;
import com.example.demo.cinema.entity.Schedule;
import com.example.demo.cinema.response.DaySchedule;
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
@RequestMapping(Routes.SCHEDULES)
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<Schedule> create(@Valid @RequestBody ScheduleDto dto) {
        return new ResponseEntity<>(
                scheduleService.create(dto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<List<DaySchedule>> getUpcomingMovieSchedules(@PathVariable long movieId) {
        return new ResponseEntity<>(
                scheduleService.findUpcomingMovieSchedules(movieId),
                HttpStatus.OK
        );
    }
}
