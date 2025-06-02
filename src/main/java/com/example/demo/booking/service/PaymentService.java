package com.example.demo.booking.service;

import com.example.demo.booking.dto.internal.BookingDto;
import com.example.demo.booking.dto.request.PaymentDto;
import com.example.demo.booking.dto.internal.PayPalOrderDto;
import com.example.demo.booking.entity.Payment;
import com.example.demo.booking.repository.PaymentDao;
import com.example.demo.booking.dto.internal.PayPalOrder;
import com.example.demo.cinema.dto.projection.SeatProjection;
import com.example.demo.cinema.service.SeatService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Service
public class PaymentService {
    private final PayPalService payPalService;
    private final BookingService bookingService;
    private final PaymentDao paymentDao;
    private final SeatService seatService;

    @Transactional
    public Payment create(PaymentDto dto, long userId) {
        List<SeatProjection> selectedSeats = seatService.findAll(dto.seatIds());

        BigDecimal totalPrice = calculatePrice(selectedSeats);
        PayPalOrderDto orderDto = new PayPalOrderDto(totalPrice);

        PayPalOrder payPalOrder = payPalService.createOrder(orderDto);

        Payment newPayment = paymentDao.save(Payment.create(
                payPalOrder.id(),
                totalPrice,
                userId
        ));

        bookingService.create(new BookingDto(
                selectedSeats,
                dto.scheduleId(),
                newPayment.getId()
        ));

        return newPayment;
    }

    private BigDecimal calculatePrice(List<SeatProjection> seats) {
        return seats
                .stream()
                .reduce(
                        BigDecimal.ZERO,
                        (sub, tot) -> sub.add(BigDecimal.valueOf(tot.type().getPrice())),
                        BigDecimal::add
                );
    }

    @Transactional
    public void capture(String payPalOrderId, long userId) {
        if (!paymentDao.isPaymentUncaptured(payPalOrderId, userId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Payment already captured.");
        }

        String payPalCaptureId = payPalService.captureOrder(payPalOrderId)
                .purchaseUnits()
                .getFirst()
                .payments()
                .captures()
                .getFirst()
                .id();

        int updatedRows = paymentDao.capture(payPalOrderId, payPalCaptureId, userId);

        if (updatedRows != 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found.");
        }
    }
}
