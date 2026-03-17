package com.mrs.app.payment.service;

import com.mrs.app.payment.component.PaymentGateway;
import com.mrs.app.payment.dto.PaymentGatewayOrderRequest;
import com.mrs.app.payment.dto.PaymentResponse;
import com.mrs.app.payment.dto.PaymentCreateRequest;
import com.mrs.app.payment.entity.Payment;
import com.mrs.app.payment.enumeration.PaymentStatus;
import com.mrs.app.payment.mapper.PaymentMapper;
import com.mrs.app.payment.repository.PaymentDAO;
import com.mrs.app.payment.util.PayPalOrderUtils;
import com.mrs.app.shared.exception.DomainRequirementError;
import com.mrs.app.shared.exception.DomainRequirementException;
import com.mrs.app.shared.exception.EntityNotFondException;
import com.mrs.app.shared.exception.EntityNotFoundError;
import com.paypal.sdk.models.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class PaymentService {
    private final PaymentGateway paymentGateway;
    private final PaymentDAO paymentDAO;
    private final PaymentMapper paymentMapper;

    public PaymentResponse create(PaymentCreateRequest createRequest) {
        Order createdOrder = paymentGateway.createOrder(new PaymentGatewayOrderRequest(
                createRequest.totalPrice(),
                "EUR"
        ));
        Payment paymentToSave = paymentMapper.toEntity(createRequest, createdOrder);
        Payment savedPayment = paymentDAO.save(paymentToSave);

        return paymentMapper.toResponse(savedPayment);
    }

    public PaymentResponse complete(long id) {
        Payment pendingPayment = findById(id);

        if (!PaymentStatus.PENDING.equals(pendingPayment.getStatus())) {
            throw new DomainRequirementException(new DomainRequirementError(
                    "The selected payment is already closed.",
                    "id"
            ));
        }

        Order order = paymentGateway.completeOrder(pendingPayment.getGatewayOrder().getOrderId());
        String captureId = PayPalOrderUtils
                .extractCaptureIdFromOrder(order)
                .orElseThrow(() -> new NoSuchElementException("The PayPal gateway hasn't returned the expected capture id."));

        pendingPayment.setStatus(PaymentStatus.COMPLETED);
        pendingPayment.getGatewayOrder().setCompletionId(captureId);

        return paymentMapper.toResponse(paymentDAO.save(pendingPayment));
    }

    public PaymentResponse refund(long id) {
        Payment payment = findById(id);

        if (!PaymentStatus.COMPLETED.equals(payment.getStatus())) {
            throw new DomainRequirementException(new DomainRequirementError(
                    "The selected payment is not completed.",
                    "id"
            ));
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        paymentGateway.refundOrder(payment.getGatewayOrder().getCompletionId());

        return paymentMapper.toResponse(paymentDAO.save(payment));
    }

    private Payment findById(long id) {
        return paymentDAO
                .findById(id)
                .orElseThrow(() -> new EntityNotFondException(new EntityNotFoundError(
                        Payment.class.getSimpleName(),
                        Map.of("id", id)
                )));
    }
}
