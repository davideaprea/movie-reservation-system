package com.mrs.app.location.controller;

import com.mrs.app.location.dto.HallCreateRequest;
import com.mrs.app.location.entity.Hall;
import com.mrs.app.location.service.HallService;
import com.mrs.app.shared.enumeration.Routes;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(Routes.HALLS)
public class HallController {
    private final HallService hallService;

    @PostMapping
    public ResponseEntity<Hall> create(HallCreateRequest createRequest) {
        return new ResponseEntity<>(
                hallService.create(createRequest),
                HttpStatus.CREATED
        );
    }
}
