package com.mrs.app.booking.controller;

import com.mrs.app.booking.dto.request.PaymentDto;
import com.mrs.app.booking.entity.Payment;
import com.mrs.app.booking.service.PaymentService;
import com.mrs.app.core.enumeration.Routes;
import com.mrs.app.security.pojo.AuthUserDetails;
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
