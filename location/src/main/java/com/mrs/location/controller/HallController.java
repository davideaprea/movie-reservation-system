package com.mrs.location.controller;

import com.mrs.location.apidoc.HallControllerDoc;
import com.mrs.location.dto.HallCreateRequest;
import com.mrs.location.dto.HallGetResponse;
import com.mrs.location.dto.HallResponse;
import com.mrs.location.service.HallService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping
public class HallController implements HallControllerDoc {
    private final HallService hallService;

    @PostMapping("/halls")
    public ResponseEntity<HallResponse> create(
            @RequestBody @Valid HallCreateRequest createRequest
    ) {
        return new ResponseEntity<>(hallService.create(createRequest), HttpStatus.CREATED);
    }

    @GetMapping("/cinemas/{cinemaId}/halls")
    public ResponseEntity<List<HallGetResponse>> findAllCinemaHalls(
            @PathVariable @Valid @Positive long cinemaId
    ) {
        return new ResponseEntity<>(hallService.findAllByCinemaId(cinemaId), HttpStatus.OK);
    }
}
