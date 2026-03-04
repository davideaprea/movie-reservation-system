package com.mrs.app.schedule.controller;

import com.mrs.app.schedule.dto.ScheduleProjection;
import com.mrs.app.schedule.dto.ScheduleDto;
import com.mrs.app.schedule.entity.Schedule;
import com.mrs.app.schedule.dto.ScheduleSeatDetails;
import com.mrs.app.schedule.mapper.ScheduleMapper;
import com.mrs.app.schedule.service.ScheduleService;
import com.mrs.app.location.service.SeatService;
import com.mrs.app.shared.enumeration.Routes;
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
    private final SeatService seatService;

    @PostMapping
    public ResponseEntity<ScheduleProjection> create(@Valid @RequestBody ScheduleDto dto) {
        Schedule savedSchedule = scheduleService.create(dto);

        return new ResponseEntity<>(
                ScheduleMapper.INSTANCE.toProjection(savedSchedule),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{scheduleId}" + Routes.SEATS)
    public ResponseEntity<List<ScheduleSeatDetails>> findScheduleSeats(@PathVariable long scheduleId) {
        return new ResponseEntity<>(
                seatService.findScheduleSeats(scheduleId),
                HttpStatus.OK
        );
    }
}
