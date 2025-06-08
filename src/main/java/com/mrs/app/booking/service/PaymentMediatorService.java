package com.mrs.app.booking.service;

import com.mrs.app.booking.dto.internal.BookingDto;
import com.mrs.app.booking.dto.internal.PaymentDto;
import com.mrs.app.booking.dto.request.BookingsPaymentDto;
import com.mrs.app.booking.entity.Payment;
import com.mrs.app.cinema.dto.projection.SeatProjection;
import com.mrs.app.cinema.service.SeatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class PaymentMediatorService {
    private final PaymentService paymentService;
    private final BookingService bookingService;
    private final SeatService seatService;

    @Transactional
    public Payment createBookingsPayment(BookingsPaymentDto dto, long userId) {
        List<SeatProjection> selectedSeats = seatService.findAll(dto.seatIds());

        PaymentDto paymentDto = new PaymentDto(userId, selectedSeats);

        Payment bookingsPayment = paymentService.create(paymentDto);

        BookingDto bookingDto = new BookingDto(
                selectedSeats,
                dto.scheduleId(),
                bookingsPayment.getId()
        );

        bookingService.create(bookingDto);

        return bookingsPayment;
    }

    @Transactional
    public void refundBookingsPayment(long paymentId, long userId) {
        paymentService.refundPayment(paymentId, userId);
        bookingService.deletePaymentBookings(paymentId, userId);
    }
}
