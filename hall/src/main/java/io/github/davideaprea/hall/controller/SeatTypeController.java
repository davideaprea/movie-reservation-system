package io.github.davideaprea.hall.controller;

import com.mrs.app.hall.apidoc.SeatTypeControllerDoc;
import com.mrs.app.hall.dto.SeatTypeCreateRequest;
import com.mrs.app.hall.dto.SeatTypeResponse;
import com.mrs.app.hall.entity.SeatType;
import com.mrs.app.hall.repository.SeatTypeDAO;
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
    private final SeatTypeDAO seatTypeDAO;

    @PostMapping
    public ResponseEntity<SeatTypeResponse> create(@RequestBody @Valid SeatTypeCreateRequest request) {
        SeatType seatType = seatTypeDAO.save(new SeatType(null, request.name()));

        return new ResponseEntity<>(
                new SeatTypeResponse(seatType.getId(), seatType.getName()),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<SeatTypeResponse>> findAll() {
        return ResponseEntity.ok(StreamSupport.stream(seatTypeDAO.findAll().spliterator(), false)
                .map(seatType -> new SeatTypeResponse(seatType.getId(), seatType.getName()))
                .toList());
    }
}
