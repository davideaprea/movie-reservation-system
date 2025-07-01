package com.mrs.app.booking.service;

import com.mrs.app.booking.constant.PaymentTimeouts;
import com.mrs.app.booking.dto.internal.BookingDto;
import com.mrs.app.booking.dto.internal.PayPalCapturedOrder;
import com.mrs.app.booking.dto.internal.PayPalOrderDto;
import com.mrs.app.booking.dto.request.BookingsPaymentDto;
import com.mrs.app.booking.entity.Payment;
import com.mrs.app.booking.repository.PaymentDao;
import com.mrs.app.booking.dto.internal.PayPalOrder;
import com.mrs.app.cinema.entity.Seat;
import com.mrs.app.cinema.service.SeatService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class PaymentService {
    private final PayPalService payPalService;
    private final PaymentDao paymentDao;
    private final BookingService bookingService;
    private final SeatService seatService;

    @Transactional
    public Payment create(BookingsPaymentDto dto, long loggedUserId) {
        List<Seat> selectedSeats = seatService.findAll(dto.seatIds());
        final BigDecimal totalPrice = calculatePrice(selectedSeats);

        PayPalOrderDto orderDto = new PayPalOrderDto(totalPrice);

        PayPalOrder payPalOrder = payPalService.createOrder(orderDto);

        Payment paymentToSave = Payment.create(payPalOrder.id(), totalPrice, loggedUserId);
        Payment savedPayment = paymentDao.save(paymentToSave);

        bookingService.create(new BookingDto(
                selectedSeats,
                dto.scheduleId(),
                savedPayment.getId()
        ));

        return savedPayment;
    }

    private BigDecimal calculatePrice(List<Seat> seatsType) {
        return seatsType.stream().reduce(
                BigDecimal.ZERO,
                (sub, tot) -> sub.add(BigDecimal.valueOf(tot.getType().getPrice())),
                BigDecimal::add
        );
    }

    public void capture(String payPalOrderId, long userId) {
        PayPalCapturedOrder capturedOrder = completePayment(payPalOrderId, userId);

        String payPalCaptureId = extractCaptureId(capturedOrder);

        paymentDao.setCaptureId(payPalOrderId, payPalCaptureId, userId);
    }

    @Transactional
    private PayPalCapturedOrder completePayment(String payPalOrderId, long userId) {
        LocalDateTime cutoff = LocalDateTime
                .now()
                .minusMinutes(PaymentTimeouts.PAYMENT_COMPLETION_TIMEOUT_MINUTES);

        int updatedRows = paymentDao.markAsCompleted(payPalOrderId, userId, cutoff);

        if (updatedRows != 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find the pending payment.");
        }

        return payPalService.captureOrder(payPalOrderId);
    }

    private String extractCaptureId(PayPalCapturedOrder payPalCapturedOrder) {
        return payPalCapturedOrder
                .purchaseUnits()
                .getFirst()
                .payments()
                .captures()
                .getFirst()
                .id();
    }

    @Transactional
    public void refundPayment(long paymentId, long userId) {
        bookingService.deletePaymentBookings(paymentId);

        paymentDao.markAsRefunded(paymentId, userId);

        Payment refundablePayment = findByIdAndUserId(paymentId, userId);

        payPalService.refundPayment(refundablePayment.getCaptureId());
    }

    public Payment findByIdAndUserId(long paymentId, long userId) {
        return paymentDao
                .findByIdAndUserId(paymentId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found."));
    }

    @Scheduled(fixedRate = 2 * 60 * 1000)
    public void deleteExpiredUncompletedPayments() {
        LocalDateTime cutoff = LocalDateTime
                .now()
                .minusMinutes(PaymentTimeouts.UNCOMPLETED_PAYMENT_GRACE_PERIOD_MINUTES);

        paymentDao.deleteExpiredUncompletedPayments(cutoff);
    }
}
