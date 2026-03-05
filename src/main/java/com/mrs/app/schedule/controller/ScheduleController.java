package com.mrs.app.schedule.controller;

import com.mrs.app.schedule.dto.ScheduleDTO;
import com.mrs.app.schedule.dto.ScheduleCreateRequest;
import com.mrs.app.schedule.service.ScheduleService;
import com.mrs.app.shared.enumeration.Routes;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(Routes.SCHEDULES)
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final SeatService seatService;

    @PostMapping
    public ResponseEntity<ScheduleDTO> create(@Valid @RequestBody ScheduleCreateRequest dto) {
        return new ResponseEntity<>(
                scheduleService.create(dto),
                HttpStatus.CREATED
        );
    }
}
