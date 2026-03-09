package com.mrs.app.hall.controller;

import com.mrs.app.hall.dto.HallCreateRequest;
import com.mrs.app.hall.entity.Hall;
import com.mrs.app.hall.service.HallService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/halls")
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
