package com.mrs.app.hall.controller;

import com.mrs.app.hall.dto.HallCreateRequest;
import com.mrs.app.hall.dto.HallGetResponse;
import com.mrs.app.hall.dto.HallResponse;
import com.mrs.app.hall.service.HallService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/halls")
public class HallController {
    private final HallService hallService;

    @PostMapping
    public ResponseEntity<HallResponse> create(HallCreateRequest createRequest) {
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
