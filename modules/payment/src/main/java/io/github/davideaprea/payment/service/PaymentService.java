package io.github.davideaprea.payment.service;

import io.github.davideaprea.payment.component.PaymentGateway;
import io.github.davideaprea.payment.dto.*;
import io.github.davideaprea.payment.dto.gateway.GatewayIntentCreateRequest;
import io.github.davideaprea.payment.dto.gateway.GatewayIntentCreateResponse;
import io.github.davideaprea.payment.entity.Completion;
import io.github.davideaprea.payment.entity.Intent;
import io.github.davideaprea.payment.mapper.PaymentMapper;
import io.github.davideaprea.payment.repository.CompletionDAO;
import io.github.davideaprea.payment.repository.IntentDAO;
import io.github.davideaprea.shared.exception.EntityNotFondException;
import io.github.davideaprea.shared.exception.EntityNotFoundError;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class PaymentService {
    private final PaymentGateway paymentGateway;
    private final IntentDAO intentDAO;
    private final CompletionDAO completionDAO;
    private final PaymentMapper paymentMapper;
    private final ModuleConfigProps configProps;

    /**
     * Creates an internal timed payment intent to be later sent to the
     * payment gateway.
     *
     * <p>The intent is persisted with the provided amount and initialized with
     * a creation timestamp and an expiration timestamp based on the configured timeout.</p>
     */
    public IntentResponse createIntent(@Valid IntentCreateRequest createRequest) {
        LocalDateTime createdAt = LocalDateTime.now();
        Intent intent = intentDAO.save(Intent.builder()
                .amount(createRequest.amount())
                .createdAt(createdAt)
                .expiresAt(createdAt.plus(configProps.timeout()))
                .build());

        return paymentMapper.toResponse(intent);
    }

    /**
     * Submits an existing internal intent to the external payment gateway,
     * propagating its identifier as metadata to allow correlation with asynchronous webhook events.
     *
     * <p>The response includes gateway-specific information required by the client
     * to proceed with the payment.</p>
     */
    public IntentSubmissionResponse submitIntent(IntentSubmissionRequest request) {
        Intent intent = intentDAO
                .findById(request.intentId())
                .filter(i -> i.getExpiresAt().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new EntityNotFondException(new EntityNotFoundError(
                        Intent.class.getSimpleName(),
                        request
                )));
        GatewayIntentCreateResponse response = paymentGateway.createIntent(new GatewayIntentCreateRequest(
                intent.getAmount(),
                "EUR",
                request.intentId()
        ));

        return new IntentSubmissionResponse(response.id(), response.nextRequiredStep(), response.clientSecret());
    }

    /**
     * Completes a payment intent in an idempotent way.
     *
     * <p>This method is typically invoked as a result of an asynchronous webhook
     * notification from the payment gateway.</p>
     *
     * <p>If a completion record for the given intent already exists, it is returned.
     * Otherwise, a new completion is created and persisted.</p>
     */
    public CompletionCreateResponse completeIntent(CompletionCreateRequest createRequest) {
        Completion completion = completionDAO
                .findByIntentId(createRequest.internalIntentId())
                .orElse(completionDAO.save(Completion.builder()
                        .intent(Intent.builder()
                                .id(createRequest.internalIntentId())
                                .build())
                        .gatewayIntentId(createRequest.gatewayIntentId())
                        .createdAt(LocalDateTime.now())
                        .build()));

        return paymentMapper.toCompletionCreateResponse(completion);
    }

    /**
     * Retrieves all expired payment intents.
     *
     * <p>An intent is considered expired if its expiration timestamp is in the past
     * and it does not have any related completion.</p>
     *
     * <p>This method is typically used by scheduled jobs to clean up stale intents
     * or trigger compensating actions.</p>
     */
    public List<IntentResponse> findExpiredIntents() {
        return intentDAO
                .findExpiredIntents().stream()
                .map(paymentMapper::toResponse).toList();
    }
}
