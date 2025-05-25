package com.example.demo.booking.controller;

import com.example.demo.booking.dto.PaymentDto;
import com.example.demo.booking.entity.Payment;
import com.example.demo.booking.service.PaymentService;
import com.example.demo.core.enumeration.Routes;
import com.example.demo.security.pojo.AuthUserDetails;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(Routes.PAYMENTS)
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> create(
            @Valid @RequestBody PaymentDto dto,
            @AuthenticationPrincipal AuthUserDetails userDetails
    ) {
        return new ResponseEntity<>(
                paymentService.create(dto, userDetails.getId()),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<Void> confirm(
            @PathVariable String orderId,
            @AuthenticationPrincipal AuthUserDetails userDetails
    ) {
        paymentService.capture(orderId, userDetails.getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
