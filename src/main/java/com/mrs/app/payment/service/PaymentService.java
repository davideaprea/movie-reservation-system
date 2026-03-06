package com.mrs.app.payment.service;

import com.mrs.app.payment.constant.PaymentTimeouts;
import com.mrs.app.payment.entity.GatewayOrder;
import com.mrs.app.payment.enumeration.PaymentStatus;
import com.mrs.app.shared.paypal.dto.PayPalCapturedOrder;
import com.mrs.app.shared.paypal.dto.PayPalOrderDto;
import com.mrs.app.payment.dto.PaymentCreateRequest;
import com.mrs.app.payment.entity.Payment;
import com.mrs.app.payment.repository.PaymentDAO;
import com.mrs.app.shared.paypal.dto.PayPalOrder;
import com.mrs.app.shared.paypal.component.PaymentGateway;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class PaymentService {
    private final PaymentGateway paymentGateway;
    private final PaymentDAO paymentDAO;

    @Transactional
    public Payment create(PaymentCreateRequest createRequest) {
        PayPalOrderDto orderDto = new PayPalOrderDto(createRequest.totalPrice());
        PayPalOrder payPalOrder = paymentGateway.createOrder(orderDto);
        GatewayOrder gatewayOrder = new GatewayOrder(payPalOrder.id(), null, createRequest.totalPrice());
        Payment paymentToSave = new Payment(null, gatewayOrder, createRequest.userId(), LocalDateTime.now(), PaymentStatus.PENDING);
        Payment savedPayment = paymentDAO.save(paymentToSave);

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
