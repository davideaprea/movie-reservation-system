package com.mrs.app.payment.controller;

import com.mrs.app.payment.dto.PaymentProjection;
import com.mrs.app.payment.dto.BookingsPaymentDto;
import com.mrs.app.payment.entity.Payment;
import com.mrs.app.payment.mapper.PaymentMapper;
import com.mrs.app.payment.service.PaymentService;
import com.mrs.app.shared.enumeration.Routes;
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
    public ResponseEntity<PaymentProjection> create(
            @Valid @RequestBody BookingsPaymentDto dto,
            @AuthenticationPrincipal AuthUserDetails userDetails
    ) {
        Payment bookingsPayment = paymentService.create(dto, userDetails.getId());

        return new ResponseEntity<>(
                PaymentMapper.INSTANCE.entityToProjection(bookingsPayment),
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

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> refund(
            @PathVariable long paymentId,
            @AuthenticationPrincipal AuthUserDetails userDetails
    ) {
        paymentService.refundPayment(paymentId, userDetails.getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
