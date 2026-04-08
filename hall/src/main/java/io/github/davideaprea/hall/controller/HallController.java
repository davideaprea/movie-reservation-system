package io.github.davideaprea.hall.controller;

import io.github.davideaprea.hall.apidoc.HallControllerDoc;
import io.github.davideaprea.hall.dto.HallCreateRequest;
import io.github.davideaprea.hall.dto.HallGetResponse;
import io.github.davideaprea.hall.dto.HallResponse;
import io.github.davideaprea.hall.service.HallService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/halls")
public class HallController implements HallControllerDoc {
    private final HallService hallService;

    @PostMapping
    public ResponseEntity<HallResponse> create(@RequestBody @Valid HallCreateRequest createRequest) {
        return new ResponseEntity<>(
                hallService.create(createRequest),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<HallGetResponse>> findAll() {
        return new ResponseEntity<>(hallService.findAll(), HttpStatus.OK);
    }
}
