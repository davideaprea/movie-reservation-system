package com.mrs.app.payment.service;

import com.mrs.app.payment.component.PaymentGateway;
import com.mrs.app.payment.dto.*;
import com.mrs.app.payment.dto.gateway.GatewayPaymentCreateRequest;
import com.mrs.app.payment.dto.gateway.GatewayPaymentCreateResponse;
import com.mrs.app.payment.dto.gateway.GatewayRefundResponse;
import com.mrs.app.payment.entity.Payment;
import com.mrs.app.payment.entity.Refund;
import com.mrs.app.payment.mapper.PaymentMapper;
import com.mrs.app.payment.repository.PaymentDAO;
import com.mrs.app.payment.repository.RefundDAO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PaymentService {
    private final PaymentGateway paymentGateway;
    private final PaymentDAO paymentDAO;
    private final RefundDAO refundDAO;
    private final PaymentMapper paymentMapper;

    public PaymentResponse pay(@Valid PaymentCreateRequest createRequest) {
        GatewayPaymentCreateResponse gatewayPayment = paymentGateway.pay(new GatewayPaymentCreateRequest(
                createRequest.totalPrice(),
                "EUR",
                createRequest.idempotencyKey()
        ));

        Payment payment;

        try {
            payment = paymentDAO.save(Payment.builder()
                    .gatewayIdempotencyKey(createRequest.idempotencyKey())
                    .gatewayPaymentId(gatewayPayment.id())
                    .price(createRequest.totalPrice())
                    .build());
        } catch (DataIntegrityViolationException e) {
            payment = paymentDAO
                    .findByGatewayPaymentId(gatewayPayment.id())
                    .orElseThrow();
        }

        return paymentMapper.toResponse(payment);
    }

    /**
     * Issues a refund for a {@link Payment}.
     */
    public RefundResponse refund(long paymentId) {
        Payment payment = paymentDAO.findById(paymentId).orElseThrow();
        GatewayRefundResponse refundResponse = paymentGateway.refund(payment.getGatewayPaymentId());
        Refund refund = refundDAO.save(Refund.builder()
                .payment(payment)
                .gatewayRefundId(refundResponse.id())
                .build());

        return new RefundResponse(refund.getId(), refund.getGatewayRefundId());
    }
}
