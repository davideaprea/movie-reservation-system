package com.mrs.app.payment.service;

import com.mrs.app.payment.component.PaymentGateway;
import com.mrs.app.payment.dto.*;
import com.mrs.app.payment.entity.Completion;
import com.mrs.app.payment.entity.Payment;
import com.mrs.app.payment.entity.Refund;
import com.mrs.app.payment.mapper.PaymentMapper;
import com.mrs.app.payment.repository.CompletionDAO;
import com.mrs.app.payment.repository.PaymentDAO;
import com.mrs.app.payment.repository.RefundDAO;
import com.mrs.app.shared.exception.ConflictingEntityException;
import com.mrs.app.shared.exception.ConflictingResourceError;
import com.mrs.app.shared.exception.EntityNotFondException;
import com.mrs.app.shared.exception.EntityNotFoundError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class PaymentService {
    private final PaymentGateway paymentGateway;
    private final PaymentDAO paymentDAO;
    private final CompletionDAO completionDAO;
    private final RefundDAO refundDAO;
    private final PaymentMapper paymentMapper;

    public PaymentResponse create(PaymentCreateRequest createRequest) {
        GatewayOrderCreateResponse createdOrder = paymentGateway.createOrder(new GatewayOrderCreateRequest(
                createRequest.totalPrice(),
                "EUR"
        ));
        Payment paymentToSave = new Payment(null, createdOrder.id(), createRequest.totalPrice());
        Payment savedPayment = paymentDAO.save(paymentToSave);

        return paymentMapper.toResponse(savedPayment);
    }

    public CompletionResponse complete(long paymentId) {
        if (completionDAO.existsByPaymentId(paymentId)) {
            throw new ConflictingEntityException(new ConflictingResourceError<>(
                    List.of(),
                    List.of("paymentId"),
                    "The payment is already completed."
            ));
        }

        Payment payment = paymentDAO
                .findById(paymentId)
                .orElseThrow(() -> new EntityNotFondException(new EntityNotFoundError(
                        Payment.class.getSimpleName(),
                        Map.of("id", paymentId)
                )));
        GatewayOrderCompletionResponse order = paymentGateway.completeOrder(payment.getGatewayOrderId());
        Completion completion = completionDAO.save(new Completion(null, payment, order.completionId()));

        return new CompletionResponse(completion.getId(), paymentId, order.completionId());
    }

    public RefundResponse refund(long completionId) {
        Completion completion = completionDAO
                .findById(completionId)
                .orElseThrow(() -> new EntityNotFondException(new EntityNotFoundError(
                        Completion.class.getSimpleName(),
                        Map.of("id", completionId)
                )));
        Refund refund = refundDAO.save(new Refund(null, completion));

        paymentGateway.refundOrder(completion.getGatewayCompletionId());

        return new RefundResponse(refund.getId(), completionId);
    }
}
