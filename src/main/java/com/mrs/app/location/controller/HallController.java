package com.mrs.app.location.controller;

import com.mrs.app.location.apidoc.HallControllerDoc;
import com.mrs.app.location.dto.HallCreateRequest;
import com.mrs.app.location.dto.HallGetResponse;
import com.mrs.app.location.dto.HallResponse;
import com.mrs.app.location.service.HallService;
import com.mrs.app.security.dto.LoggedUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping
public class HallController implements HallControllerDoc {
    private final HallService hallService;

    @PostMapping("/halls")
    public ResponseEntity<HallResponse> create(
            @RequestBody @Valid HallCreateRequest createRequest,
            @AuthenticationPrincipal LoggedUser loggedUser
    ) {
        return new ResponseEntity<>(hallService.create(loggedUser, createRequest), HttpStatus.CREATED);
    }

    @GetMapping("/cinemas/{cinemaId}/halls")
    public ResponseEntity<List<HallGetResponse>> findAllCinemaHalls(
            @AuthenticationPrincipal LoggedUser loggedUser,
            @PathVariable @Valid @Positive long cinemaId
    ) {
        return new ResponseEntity<>(hallService.findAllByCinemaId(loggedUser, cinemaId), HttpStatus.OK);
    }
}
