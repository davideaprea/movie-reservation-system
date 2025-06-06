package com.mrs.app.booking.service;

import com.mrs.app.booking.dto.internal.BookingDto;
import com.mrs.app.booking.dto.internal.PayPalCapturedOrder;
import com.mrs.app.booking.dto.projection.PaymentProjection;
import com.mrs.app.booking.dto.request.PaymentDto;
import com.mrs.app.booking.dto.internal.PayPalOrderDto;
import com.mrs.app.booking.entity.Payment;
import com.mrs.app.booking.repository.PaymentDao;
import com.mrs.app.booking.dto.internal.PayPalOrder;
import com.mrs.app.cinema.dto.projection.ScheduleProjection;
import com.mrs.app.cinema.dto.projection.SeatProjection;
import com.mrs.app.cinema.service.ScheduleService;
import com.mrs.app.cinema.service.SeatService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class PaymentService {
    private final PayPalService payPalService;
    private final BookingService bookingService;
    private final PaymentDao paymentDao;
    private final SeatService seatService;
    private final PayPalUtilityService payPalUtilityService;
    private final ScheduleService scheduleService;

    @Transactional
    public Payment create(PaymentDto dto, long userId) {
        List<SeatProjection> selectedSeats = seatService.findAll(dto.seatIds());

        BigDecimal totalPrice = calculatePrice(selectedSeats);
        PayPalOrderDto orderDto = new PayPalOrderDto(totalPrice);

        PayPalOrder payPalOrder = payPalService.createOrder(orderDto);

        Payment paymentToSave = Payment.create(payPalOrder.id(), totalPrice, userId);

        Payment savedPayment = paymentDao.save(paymentToSave);

        BookingDto bookingDto = new BookingDto(selectedSeats, dto.scheduleId(), savedPayment.getId());

        bookingService.create(bookingDto);

        return savedPayment;
    }

    private BigDecimal calculatePrice(List<SeatProjection> seats) {
        return seats.stream().reduce(
                BigDecimal.ZERO,
                (sub, tot) -> sub.add(BigDecimal.valueOf(tot.type().getPrice())),
                BigDecimal::add
        );
    }

    public void capture(String payPalOrderId, long userId) {
        PayPalCapturedOrder capturedOrder = completePayment(payPalOrderId, userId);

        String payPalCaptureId = payPalUtilityService.extractCaptureId(capturedOrder);

        paymentDao.setCaptureId(payPalOrderId, payPalCaptureId, userId);
    }

    @Transactional
    private PayPalCapturedOrder completePayment(String payPalOrderId, long userId) {
        final int paymentExpiryMinutes = 5;
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(paymentExpiryMinutes);

        int updatedRows = paymentDao.markAsCompleted(payPalOrderId, userId, cutoff);

        if (updatedRows != 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find the pending payment.");
        }

        return payPalService.captureOrder(payPalOrderId);
    }

    @Transactional
    public void refundPayment(long paymentId, long userId) {
        ScheduleProjection paymentSchedule = scheduleService.findPaymentSchedule(paymentId);

        final LocalDateTime now = LocalDateTime.now();

        final long refundExpiryTime = 3;

        Duration hoursDiff = Duration.between(now, paymentSchedule.startTime());

        if(
                paymentSchedule.startTime().isAfter(now) ||
                hoursDiff.toHours() >= refundExpiryTime
        ) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Refunds can be requested within 3 hours of the start of the schedule."
            );
        }

        bookingService.deletePaymentBookings(paymentId);
        paymentDao.markAsRefunded(paymentId, userId);

        PaymentProjection refundablePayment = findProjectionById(paymentId, userId);

        payPalService.refundPayment(refundablePayment.captureId());
    }

    public PaymentProjection findProjectionById(long paymentId, long userId) {
        return paymentDao
                .findProjectionById(paymentId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found."));
    }

    @Scheduled(fixedRate = 2 * 60 * 1000)
    public void deleteExpiredUncompletedPayments() {
        final int safePaymentExpiryMinutes = 7;
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(safePaymentExpiryMinutes);

        paymentDao.deleteExpiredUncompletedPayments(cutoff);
    }
}
