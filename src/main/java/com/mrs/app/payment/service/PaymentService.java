package com.mrs.app.payment.service;

import com.mrs.app.payment.component.PaymentGateway;
import com.mrs.app.payment.dto.*;
import com.mrs.app.payment.dto.gateway.GatewayOrderCompletionResponse;
import com.mrs.app.payment.dto.gateway.GatewayIntentCreateRequest;
import com.mrs.app.payment.dto.gateway.GatewayIntentCreateResponse;
import com.mrs.app.payment.entity.Completion;
import com.mrs.app.payment.entity.Intent;
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

    public IntentResponse create(IntentCreateRequest createRequest) {
        GatewayIntentCreateResponse createdIntent = paymentGateway.createIntent(new GatewayIntentCreateRequest(
                createRequest.totalPrice(),
                "EUR"
        ));
        Intent intentToSave = new Intent(null, createdIntent.id(), createRequest.totalPrice());
        Intent savedIntent = paymentDAO.save(intentToSave);

        return paymentMapper.toResponse(savedIntent);
    }

    public CompletionResponse complete(long paymentId) {
        if (completionDAO.existsByPaymentId(paymentId)) {
            throw new ConflictingEntityException(new ConflictingResourceError<>(
                    List.of(),
                    List.of("paymentId"),
                    "The intent is already completed."
            ));
        }

        Intent intent = paymentDAO
                .findById(paymentId)
                .orElseThrow(() -> new EntityNotFondException(new EntityNotFoundError(
                        Intent.class.getSimpleName(),
                        Map.of("id", paymentId)
                )));
        GatewayOrderCompletionResponse gatewayPaymentCompletion = paymentGateway.completePayment(intent.getGatewayOrderId());
        Completion completion = completionDAO.save(new Completion(null, intent, gatewayPaymentCompletion.completionId()));

        return new CompletionResponse(completion.getId(), paymentId, gatewayPaymentCompletion.completionId());
    }

    public RefundResponse refund(long completionId) {
        Completion completion = completionDAO
                .findById(completionId)
                .orElseThrow(() -> new EntityNotFondException(new EntityNotFoundError(
                        Completion.class.getSimpleName(),
                        Map.of("id", completionId)
                )));
        Refund refund = refundDAO.save(new Refund(null, completion));

        paymentGateway.refundPayment(completion.getGatewayCompletionId());

        return new RefundResponse(refund.getId(), completionId);
    }
}
