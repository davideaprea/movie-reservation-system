package io.github.davideaprea.order.controller;

import io.github.davideaprea.order.apidoc.OrderControllerDoc;
import io.github.davideaprea.order.dto.*;
import io.github.davideaprea.order.service.OrderService;
import io.github.davideaprea.security.dto.AuthUserDetails;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController implements OrderControllerDoc {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderCreateResponse> create(
            @RequestBody @Valid HTTPOrderCreateRequest request,
            @AuthenticationPrincipal AuthUserDetails loggedUser
    ) {
        return new ResponseEntity<>(orderService.create(new OrderCreateRequest(
                loggedUser.getId(),
                request.scheduleId(),
                request.seatIds()
        )), HttpStatus.CREATED);
    }
}
