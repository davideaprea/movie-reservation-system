package com.mrs.app.schedule.controller;

import com.mrs.app.schedule.dto.ScheduleResponse;
import com.mrs.app.schedule.dto.ScheduleCreateRequest;
import com.mrs.app.schedule.dto.SchedulesGetFilters;
import com.mrs.app.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponse> create(@Valid @RequestBody ScheduleCreateRequest dto) {
        return new ResponseEntity<>(
                scheduleService.create(dto),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> findByFilters(@ModelAttribute SchedulesGetFilters filters) {
        return new ResponseEntity<>(scheduleService.findByFilters(filters), HttpStatus.OK);
    }
}
