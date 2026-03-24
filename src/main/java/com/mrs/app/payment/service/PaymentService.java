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
        Intent intentToSave = Intent.builder()
                .gatewayIntentId(createdIntent.id())
                .price(createRequest.totalPrice())
                .build();
        Intent savedIntent = paymentDAO.save(intentToSave);

        return paymentMapper.toResponse(savedIntent);
    }

    public CompletionResponse complete(long intentId) {
        if (completionDAO.existsByIntentId(intentId)) {
            throw new ConflictingEntityException(new ConflictingResourceError<>(
                    List.of(),
                    List.of("intentId"),
                    "The intent is already completed."
            ));
        }

        Intent intent = paymentDAO
                .findById(intentId)
                .orElseThrow(() -> new EntityNotFondException(new EntityNotFoundError(
                        Intent.class.getSimpleName(),
                        Map.of("id", intentId)
                )));
        GatewayOrderCompletionResponse gatewayPaymentCompletion = paymentGateway.completePayment(intent.getGatewayIntentId());
        Completion completion = completionDAO.save(Completion.builder()
                .gatewayCompletionId(gatewayPaymentCompletion.completionId())
                .intent(intent)
                .build());

        return new CompletionResponse(completion.getId(), intentId, gatewayPaymentCompletion.completionId());
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
