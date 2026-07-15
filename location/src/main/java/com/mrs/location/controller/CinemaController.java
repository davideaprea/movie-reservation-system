package com.mrs.location.controller;

import com.mrs.location.apidoc.CinemaControllerDoc;
import com.mrs.location.dto.CinemaCreateRequest;
import com.mrs.location.dto.CinemaResponse;
import com.mrs.location.service.CinemaService;
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
public class CinemaController implements CinemaControllerDoc {
    private final CinemaService cinemaService;

    @PostMapping
    public ResponseEntity<CinemaResponse> create(@RequestBody @Valid CinemaCreateRequest request) {
        return new ResponseEntity<>(cinemaService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<CinemaResponse>> findAllByCityId(
            Pageable pageable,
            @RequestParam long cityId
    ) {
        return new ResponseEntity<>(cinemaService.findAllByCityId(pageable, cityId), HttpStatus.OK);
    }
}
