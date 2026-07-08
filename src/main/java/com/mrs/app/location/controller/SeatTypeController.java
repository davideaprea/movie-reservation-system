package com.mrs.app.location.controller;

import com.mrs.app.location.apidoc.SeatTypeControllerDoc;
import com.mrs.app.location.dto.SeatTypeCreateRequest;
import com.mrs.app.location.dto.SeatTypeResponse;
import com.mrs.app.location.entity.SeatType;
import com.mrs.app.location.repository.SeatTypeRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@AllArgsConstructor
@RequestMapping("/seat-types")
public class SeatTypeController implements SeatTypeControllerDoc {
    private final SeatTypeRepository seatTypeRepository;

    @PostMapping
    public ResponseEntity<SeatTypeResponse> create(@RequestBody @Valid SeatTypeCreateRequest request) {
        SeatType seatType = seatTypeRepository.save(new SeatType(null, request.name()));

        return new ResponseEntity<>(
                new SeatTypeResponse(seatType.getId(), seatType.getName()),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<SeatTypeResponse>> findAll() {
        return ResponseEntity.ok(StreamSupport.stream(seatTypeRepository.findAll().spliterator(), false)
                .map(seatType -> new SeatTypeResponse(seatType.getId(), seatType.getName()))
                .toList());
    }
}
