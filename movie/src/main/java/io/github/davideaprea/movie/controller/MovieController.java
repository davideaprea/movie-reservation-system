package io.github.davideaprea.movie.controller;

import io.github.davideaprea.movie.apidoc.MovieControllerDoc;
import io.github.davideaprea.movie.dto.MovieResponse;
import io.github.davideaprea.movie.dto.MovieCreateRequest;
import io.github.davideaprea.movie.service.MovieService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/movies")
public class MovieController implements MovieControllerDoc {
    private final MovieService movieService;

    @PostMapping
    public ResponseEntity<MovieResponse> create(@Valid @RequestBody MovieCreateRequest dto) {
        return new ResponseEntity<>(
                movieService.create(dto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> findById(@PathVariable long id) {
        return new ResponseEntity<>(
                movieService.findById(id),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<MovieResponse>> findAll(@RequestParam(required = false) String title) {
        return new ResponseEntity<>(
                movieService.findAllByTitle(title),
                HttpStatus.OK
        );
    }
}
