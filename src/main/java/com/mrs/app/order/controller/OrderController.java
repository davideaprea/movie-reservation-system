package com.mrs.app.order.controller;

import com.mrs.app.order.dto.*;
import com.mrs.app.order.service.OrderService;
import com.mrs.app.security.dto.AuthUserDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderCreateResponse> create(@RequestBody @Valid OrderCreateRequest request) {
        return new ResponseEntity<>(orderService.create(request), HttpStatus.CREATED);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderCompletionResponse> complete(
            @PathVariable @Valid @Positive long orderId,
            @AuthenticationPrincipal AuthUserDetails loggedUser
    ) {
        return new ResponseEntity<>(orderService.complete(new OrderUpdateRequest(loggedUser.getId(), orderId)), HttpStatus.OK);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderCancellationResponse> cancel(
            @PathVariable @Valid @Positive long orderId,
            @AuthenticationPrincipal AuthUserDetails loggedUser
    ) {
        return new ResponseEntity<>(orderService.cancel(new OrderUpdateRequest(loggedUser.getId(), orderId)), HttpStatus.OK);
    }
}
