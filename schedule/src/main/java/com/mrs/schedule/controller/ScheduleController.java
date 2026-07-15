package com.mrs.schedule.controller;

import com.mrs.schedule.apidoc.ScheduleControllerDoc;
import com.mrs.schedule.dto.ScheduleGetResponse;
import com.mrs.schedule.dto.ScheduleResponse;
import com.mrs.schedule.dto.ScheduleCreateRequest;
import com.mrs.schedule.dto.ScheduleGetRequestFilters;
import com.mrs.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/schedules")
public class ScheduleController implements ScheduleControllerDoc {
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponse> create(
            @Valid @RequestBody ScheduleCreateRequest dto
    ) {
        return new ResponseEntity<>(scheduleService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleGetResponse>> findAllByFilters(@ModelAttribute @Valid ScheduleGetRequestFilters filters) {
        return new ResponseEntity<>(scheduleService.findAllByFilters(filters), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> findById(@PathVariable long id) {
        return new ResponseEntity<>(scheduleService.findById(id), HttpStatus.OK);
    }
}
