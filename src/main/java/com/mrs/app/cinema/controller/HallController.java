package com.mrs.app.cinema.controller;

import com.mrs.app.cinema.dto.request.HallDto;
import com.mrs.app.cinema.entity.Hall;
import com.mrs.app.cinema.service.HallService;
import com.mrs.app.routes.ControllerRoutes;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(ControllerRoutes.HALLS)
public class HallController {
    private final HallService hallService;

    @PostMapping
    public ResponseEntity<Hall> create(HallDto dto) {
        return new ResponseEntity<>(
                hallService.create(dto),
                HttpStatus.CREATED
        );
    }
}
