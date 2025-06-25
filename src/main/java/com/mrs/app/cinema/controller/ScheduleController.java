package com.mrs.app.cinema.controller;

import com.mrs.app.cinema.dto.projection.ScheduleProjection;
import com.mrs.app.cinema.dto.request.ScheduleDto;
import com.mrs.app.cinema.entity.Schedule;
import com.mrs.app.cinema.dto.projection.ScheduleSeatDetails;
import com.mrs.app.cinema.mapper.ScheduleMapper;
import com.mrs.app.cinema.service.ScheduleService;
import com.mrs.app.cinema.service.SeatService;
import com.mrs.app.routes.ControllerRoutes;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(ControllerRoutes.SCHEDULES)
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final SeatService seatService;

    @PostMapping
    public ResponseEntity<ScheduleProjection> create(@Valid @RequestBody ScheduleDto dto) {
        Schedule savedSchedule = scheduleService.create(dto);

        return new ResponseEntity<>(
                ScheduleMapper.INSTANCE.toProjection(savedSchedule),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{scheduleId}" + ControllerRoutes.SEATS)
    public ResponseEntity<List<ScheduleSeatDetails>> findScheduleSeats(@PathVariable long scheduleId) {
        return new ResponseEntity<>(
                seatService.findScheduleSeats(scheduleId),
                HttpStatus.OK
        );
    }
}
