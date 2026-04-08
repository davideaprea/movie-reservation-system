package io.github.davideaprea.controller;

import com.mrs.app.order.apidoc.OrderControllerDoc;
import com.mrs.app.order.dto.*;
import com.mrs.app.order.service.OrderService;
import com.mrs.app.security.dto.AuthUserDetails;
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
