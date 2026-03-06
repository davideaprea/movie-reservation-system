package com.mrs.app.payment.service;

import com.mrs.app.payment.constant.PaymentTimeouts;
import com.mrs.app.payment.dto.PaymentCompletionRequest;
import com.mrs.app.payment.dto.PaymentCreateResponse;
import com.mrs.app.payment.dto.PaymentCreateRequest;
import com.mrs.app.payment.entity.Payment;
import com.mrs.app.payment.enumeration.PaymentStatus;
import com.mrs.app.payment.mapper.PayPalOrderMapper;
import com.mrs.app.payment.mapper.PaymentMapper;
import com.mrs.app.payment.repository.PaymentDAO;
import com.paypal.sdk.PaypalServerSdkClient;
import com.paypal.sdk.models.*;
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
    private final PaypalServerSdkClient paymentGateway;
    private final PaymentDAO paymentDAO;
    private final PaymentMapper paymentMapper;
    private final PayPalOrderMapper payPalOrderMapper;

    public PaymentCreateResponse create(PaymentCreateRequest createRequest) {
        Order createdOrder = paymentGateway
                .getOrdersController()
                .createOrder(payPalOrderMapper.toCreateOrderInput(createRequest))
                .getResult();
        Payment paymentToSave = paymentMapper.toEntity(createRequest, createdOrder);
        Payment savedPayment = paymentDAO.save(paymentToSave);

        return paymentMapper.toResponse(savedPayment);
    }

    public void complete(PaymentCompletionRequest completionRequest) {
        Payment pendingPayment = paymentDAO
                .findByIdAndUserId(completionRequest.paymentId(), completionRequest.userId())
                .orElseThrow();

        if (!PaymentStatus.PENDING.equals(pendingPayment.getStatus())) {
            //throw
        }

        Order capturedOrder = paymentGateway.getOrdersController().captureOrder(new CaptureOrderInput
                .Builder()
                .id(pendingPayment.getGatewayOrder().getId())
                .build());
        pendingPayment.setStatus(PaymentStatus.COMPLETED);
        pendingPayment.getGatewayOrder().setCompletionId(capturedOrder.getPaymentSource().);
        paymentDAO.save(pendingPayment);
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
}
