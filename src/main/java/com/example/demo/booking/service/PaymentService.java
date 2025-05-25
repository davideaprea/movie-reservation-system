package com.example.demo.booking.service;

import com.example.demo.booking.dto.BookingDto;
import com.example.demo.booking.dto.OrderDto;
import com.example.demo.booking.entity.Payment;
import com.example.demo.booking.repository.PaymentDao;
import com.example.demo.booking.response.PayPalOrder;
import com.example.demo.cinema.projection.SeatProjection;
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
    public Payment create(BookingDto dto, long userId) {
        List<SeatProjection> selectedSeats = seatService.findAll(dto.seatIds());

        BigDecimal totalPrice = calculatePrice(selectedSeats);
        OrderDto orderDto = new OrderDto(totalPrice);

        PayPalOrder payPalOrder = payPalService.createOrder(orderDto);

        Payment payment = paymentDao.save(Payment.create(
                payPalOrder.id(),
                totalPrice,
                userId
        ));

        bookingService.create(selectedSeats, dto.scheduleId(), payment.getId());

        return payment;
    }

    @Transactional
    public void capture(String orderId, long userId) {
        if (!paymentDao.isPaymentUncaptured(orderId, userId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Payment already captured.");
        }

        String captureId = payPalService.captureOrder(orderId);
        int updatedRows = paymentDao.capture(orderId, captureId, userId);

        if (updatedRows != 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found.");
        }
    }

    private BigDecimal calculatePrice(List<SeatProjection> seats) {
        return seats
                .stream()
                .reduce(
                        BigDecimal.ZERO,
                        (sub, tot) -> sub.add(BigDecimal.valueOf(tot.seatType().getPrice())),
                        BigDecimal::add
                );
    }
}
