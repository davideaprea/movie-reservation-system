package com.mrs.app.location.controller;

import com.mrs.app.location.apidoc.RegionControllerDoc;
import com.mrs.app.location.dto.RegionResponse;
import com.mrs.app.location.mapper.RegionMapper;
import com.mrs.app.location.repository.RegionRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@AllArgsConstructor
@RequestMapping("/regions")
public class RegionController implements RegionControllerDoc {
    private final RegionRepository regionRepository;
    private final RegionMapper regionMapper;

    @GetMapping
    public ResponseEntity<List<RegionResponse>> findAll() {
        return new ResponseEntity<>(
                StreamSupport.stream(regionRepository.findAll().spliterator(), false)
                        .map(regionMapper::toResponse)
                        .toList(),
                HttpStatus.OK
        );
    }
}
