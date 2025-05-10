package com.example.demo.cinema.controller;

import com.example.demo.cinema.dto.MovieDto;
import com.example.demo.cinema.entity.Movie;
import com.example.demo.cinema.service.MovieService;
import com.example.demo.core.enumeration.Routes;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController(Routes.MOVIES)
public class MovieController {
    private final MovieService movieService;

    @PostMapping
    public ResponseEntity<Movie> create(@Valid MovieDto dto) {
        return new ResponseEntity<>(
                movieService.create(dto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getById(@PathVariable long id) {
        return new ResponseEntity<>(
                movieService.getById(id),
                HttpStatus.OK
        );
    }
}
