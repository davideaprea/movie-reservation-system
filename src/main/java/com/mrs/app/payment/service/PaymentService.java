package com.mrs.app.payment.service;

import com.mrs.app.booking.service.BookingService;
import com.mrs.app.payment.constant.PaymentTimeouts;
import com.mrs.app.booking.dto.BookingCreateRequest;
import com.mrs.app.shared.dto.PayPalCapturedOrder;
import com.mrs.app.shared.dto.PayPalOrderDto;
import com.mrs.app.payment.dto.BookingsPaymentDto;
import com.mrs.app.payment.entity.Payment;
import com.mrs.app.payment.repository.PaymentDAO;
import com.mrs.app.shared.dto.PayPalOrder;
import com.mrs.app.location.entity.Seat;
import com.mrs.app.shared.component.PaymentGateway;
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
    private final PaymentGateway paymentGateway;
    private final PaymentDAO paymentDAO;
    private final BookingService bookingService;
    private final SeatService seatService;

    @Transactional
    public Payment create(BookingsPaymentDto dto, long loggedUserId) {
        List<Seat> selectedSeats = seatService.findAll(dto.seatIds());
        final BigDecimal totalPrice = selectedSeats.stream().reduce(
                BigDecimal.ZERO,
                (sub, tot) -> sub.add(tot.getType().getPrice()),
                BigDecimal::add
        );
        PayPalOrderDto orderDto = new PayPalOrderDto(totalPrice);
        PayPalOrder payPalOrder = paymentGateway.createOrder(orderDto);
        Payment paymentToSave = Payment.create(payPalOrder.id(), totalPrice, loggedUserId);
        Payment savedPayment = paymentDAO.save(paymentToSave);

        bookingService.create(new BookingCreateRequest(
                selectedSeats,
                dto.scheduleId(),
                savedPayment.getId()
        ));

        return savedPayment;
    }

    public void capture(String payPalOrderId, long userId) {
        PayPalCapturedOrder capturedOrder = completePayment(payPalOrderId, userId);

        String payPalCaptureId = extractCaptureId(capturedOrder);

        paymentDAO.setCaptureId(payPalOrderId, payPalCaptureId, userId);
    }

    @Transactional
    private PayPalCapturedOrder completePayment(String payPalOrderId, long userId) {
        LocalDateTime cutoff = LocalDateTime
                .now()
                .minusMinutes(PaymentTimeouts.PAYMENT_COMPLETION_TIMEOUT_MINUTES);

        int updatedRows = paymentDAO.markAsCompleted(payPalOrderId, userId, cutoff);

        if (updatedRows != 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find the pending payment.");
        }

        return paymentGateway.captureOrder(payPalOrderId);
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

        paymentDAO.markAsRefunded(paymentId, userId);

        Payment refundablePayment = findByIdAndUserId(paymentId, userId);

        paymentGateway.refundPayment(refundablePayment.getCaptureId());
    }

    public Payment findByIdAndUserId(long paymentId, long userId) {
        return paymentDAO
                .findByIdAndUserId(paymentId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found."));
    }

    @Scheduled(fixedRate = 2 * 60 * 1000)
    public void deleteExpiredUncompletedPayments() {
        LocalDateTime cutoff = LocalDateTime
                .now()
                .minusMinutes(PaymentTimeouts.UNCOMPLETED_PAYMENT_GRACE_PERIOD_MINUTES);

        paymentDAO.deleteExpiredUncompletedPayments(cutoff);
    }
}
