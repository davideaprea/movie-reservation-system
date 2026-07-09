package com.mrs.app.location.controller;

import com.mrs.app.location.dto.CinemaCreateRequest;
import com.mrs.app.location.dto.CinemaResponse;
import com.mrs.app.location.service.CinemaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/cinemas")
public class CinemaController {
    private final CinemaService cinemaService;

    @PostMapping
    public ResponseEntity<CinemaResponse> create(@RequestBody @Valid CinemaCreateRequest request) {
        return new ResponseEntity<>(cinemaService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<CinemaResponse>> findAll(Pageable pageable) {
        return new ResponseEntity<>(cinemaService.findAll(pageable), HttpStatus.OK);
    }
}
