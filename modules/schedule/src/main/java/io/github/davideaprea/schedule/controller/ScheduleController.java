package io.github.davideaprea.schedule.controller;

import io.github.davideaprea.schedule.apidoc.ScheduleControllerDoc;
import io.github.davideaprea.schedule.dto.ScheduleResponse;
import io.github.davideaprea.schedule.dto.ScheduleCreateRequest;
import io.github.davideaprea.schedule.dto.ScheduleGetRequestFilters;
import io.github.davideaprea.schedule.service.ScheduleService;
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
    public ResponseEntity<ScheduleResponse> create(@Valid @RequestBody ScheduleCreateRequest dto) {
        return new ResponseEntity<>(scheduleService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> findAllByFilters(@ModelAttribute @Valid ScheduleGetRequestFilters filters) {
        return new ResponseEntity<>(scheduleService.findAllByFilters(filters), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> findById(@PathVariable long id) {
        return new ResponseEntity<>(scheduleService.findById(id), HttpStatus.OK);
    }
}
