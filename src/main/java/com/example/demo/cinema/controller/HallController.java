package com.example.demo.cinema.controller;

import com.example.demo.cinema.dto.HallDto;
import com.example.demo.cinema.entity.Hall;
import com.example.demo.cinema.service.HallService;
import com.example.demo.core.enumeration.Routes;
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
    public ResponseEntity<Hall> create(HallDto dto) {
        return new ResponseEntity<>(
                hallService.create(dto),
                HttpStatus.CREATED
        );
    }
}
