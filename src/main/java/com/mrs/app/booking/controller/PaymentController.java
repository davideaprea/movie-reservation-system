package com.mrs.app.booking.controller;

import com.mrs.app.booking.dto.internal.BookingDto;
import com.mrs.app.booking.dto.internal.PaymentDto;
import com.mrs.app.booking.dto.request.BookingsPaymentDto;
import com.mrs.app.booking.entity.Payment;
import com.mrs.app.booking.service.BookingService;
import com.mrs.app.booking.service.PaymentService;
import com.mrs.app.cinema.dto.projection.SeatProjection;
import com.mrs.app.cinema.enumeration.SeatType;
import com.mrs.app.cinema.service.SeatService;
import com.mrs.app.core.enumeration.Routes;
import com.mrs.app.security.pojo.AuthUserDetails;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(Routes.PAYMENTS)
public class PaymentController {
    private final PaymentService paymentService;
    private final BookingService bookingService;
    private final SeatService seatService;

    @Transactional
    @PostMapping
    public ResponseEntity<Payment> create(
            @Valid @RequestBody BookingsPaymentDto dto,
            @AuthenticationPrincipal AuthUserDetails userDetails
    ) {
        List<SeatProjection> selectedSeats = seatService.findAll(dto.seatIds());

        PaymentDto paymentDto = new PaymentDto(
                userDetails.getId(),
                extractSeatTypes(selectedSeats)
        );

        Payment bookingsPayment = paymentService.create(paymentDto);

        BookingDto bookingDto = new BookingDto(
                selectedSeats,
                dto.scheduleId(),
                bookingsPayment.getId()
        );

        bookingService.create(bookingDto);

        return new ResponseEntity<>(bookingsPayment, HttpStatus.CREATED);
    }

    private List<SeatType> extractSeatTypes(List<SeatProjection> seatProjections) {
        return seatProjections
                .stream()
                .map(SeatProjection::type)
                .toList();
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<Void> confirm(
            @PathVariable String orderId,
            @AuthenticationPrincipal AuthUserDetails userDetails
    ) {
        paymentService.capture(orderId, userDetails.getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Transactional
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> refund(
            @PathVariable long paymentId,
            @AuthenticationPrincipal AuthUserDetails userDetails
    ) {
        paymentService.refundPayment(paymentId, userDetails.getId());
        bookingService.deletePaymentBookings(paymentId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
