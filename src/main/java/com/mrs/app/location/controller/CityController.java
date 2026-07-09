package com.mrs.app.location.controller;

import com.mrs.app.location.dto.CityResponse;
import com.mrs.app.location.service.CityService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@AllArgsConstructor
public class CityController {
    private final CityService cityService;

    @GetMapping("/{regionId}/cities")
    public ResponseEntity<Page<CityResponse>> findAllByRegionId(
            @PathVariable @Valid @Positive long regionId,
            Pageable pageable
    ) {
        return new ResponseEntity<>(
                cityService.findAllByRegionId(pageable, regionId),
                HttpStatus.OK
        );
    }
}
