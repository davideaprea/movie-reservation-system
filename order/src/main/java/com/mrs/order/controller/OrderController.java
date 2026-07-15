package com.mrs.order.controller;

import com.mrs.order.apidoc.OrderControllerDoc;
import com.mrs.order.dto.HTTPOrderCreateRequest;
import com.mrs.order.dto.OrderCreateRequest;
import com.mrs.order.dto.OrderCreateResponse;
import com.mrs.order.dto.OrderGetResponse;
import com.mrs.order.service.OrderService;
import com.mrs.shared.model.CurrentUserProvider;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController implements OrderControllerDoc {
    private final OrderService orderService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping
    public ResponseEntity<OrderCreateResponse> create(
            @RequestBody @Valid HTTPOrderCreateRequest request
    ) {
        return new ResponseEntity<>(orderService.create(new OrderCreateRequest(
                currentUserProvider.get().get().id(),
                request.scheduleId(),
                request.seatIds()
        )), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderGetResponse>> findAllByUserId() {
        return ResponseEntity.ok(orderService.findAllByUserId(
                currentUserProvider.get().get().id()
        ));
    }
}
