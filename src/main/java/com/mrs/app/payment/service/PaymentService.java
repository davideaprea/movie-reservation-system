package com.mrs.app.payment.service;

import com.mrs.app.payment.component.PaymentGateway;
import com.mrs.app.payment.dto.*;
import com.mrs.app.payment.dto.gateway.GatewayPaymentCreateRequest;
import com.mrs.app.payment.dto.gateway.GatewayPaymentCreateResponse;
import com.mrs.app.payment.entity.Payment;
import com.mrs.app.payment.entity.Refund;
import com.mrs.app.payment.mapper.PaymentMapper;
import com.mrs.app.payment.repository.PaymentDAO;
import com.mrs.app.payment.repository.RefundDAO;
import com.mrs.app.shared.exception.ConflictingEntityException;
import com.mrs.app.shared.exception.ConflictingResourceError;
import com.mrs.app.shared.exception.EntityNotFondException;
import com.mrs.app.shared.exception.EntityNotFoundError;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class PaymentService {
    private final PaymentGateway paymentGateway;
    private final PaymentDAO paymentDAO;
    private final RefundDAO refundDAO;
    private final PaymentMapper paymentMapper;

    public PaymentResponse pay(@Valid PaymentCreateRequest createRequest) {
        Payment payment;

        try {
            payment = paymentDAO.save(Payment.builder()
                    .gatewayIdempotencyKey(createRequest.idempotencyKey())
                    .price(createRequest.totalPrice())
                    .build());
        } catch (DataIntegrityViolationException e) {
            payment = paymentDAO
                    .findByIdempotencyKey(createRequest.idempotencyKey())
                    .orElseThrow();
        }

        GatewayPaymentCreateResponse gatewayPayment = paymentGateway.pay(new GatewayPaymentCreateRequest(
                createRequest.totalPrice(),
                "EUR",
                createRequest.idempotencyKey()
        ));

        payment.setGatewayPaymentId(gatewayPayment.id());
        paymentDAO.save(payment);

        return paymentMapper.toResponse(payment);
    }

    /**
     * Issues a refund for a payment {@link Completion}.
     *
     * @throws EntityNotFondException     if the completion does not exist
     * @throws ConflictingEntityException if the payment has already been refunded
     */
    public RefundResponse refund(long completionId) {
        Completion completion = completionDAO
                .findById(completionId)
                .orElseThrow(() -> new EntityNotFondException(new EntityNotFoundError(
                        Completion.class.getSimpleName(),
                        Map.of("id", completionId)
                )));
        Refund refund;

        try {
            refund = refundDAO.save(new Refund(null, completion));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictingEntityException(new ConflictingResourceError<>(
                    List.of(),
                    List.of("completionId"),
                    "The selected payment has already been refunded."
            ));
        }

        paymentGateway.refundPayment(completion.getGatewayCompletionId());

        return new RefundResponse(refund.getId(), completionId);
    }
}
