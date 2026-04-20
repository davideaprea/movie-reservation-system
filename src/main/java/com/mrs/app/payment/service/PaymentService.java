package com.mrs.app.payment.service;

import com.mrs.app.payment.component.PaymentGateway;
import com.mrs.app.payment.dto.*;
import com.mrs.app.payment.dto.gateway.GatewayIntentCreateRequest;
import com.mrs.app.payment.dto.gateway.GatewayIntentCreateResponse;
import com.mrs.app.payment.entity.Completion;
import com.mrs.app.payment.entity.Intent;
import com.mrs.app.payment.mapper.PaymentMapper;
import com.mrs.app.payment.repository.CompletionRepository;
import com.mrs.app.payment.repository.IntentRepository;
import com.mrs.app.shared.exception.EntityNotFoundError;
import com.mrs.app.shared.exception.EntityNotFoundException;
import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

@Slf4j
@AllArgsConstructor
@Service
public class PaymentService {
    private final PaymentGateway paymentGateway;
    private final IntentRepository intentRepository;
    private final CompletionRepository completionRepository;
    private final PaymentMapper paymentMapper;
    private final ModuleConfigProps configProps;

    /**
     * Creates an internal timed payment intent to be later sent to the
     * payment gateway.
     *
     * <p>The intent is persisted with the provided amount and initialized with
     * a creation timestamp and an expiration timestamp based on the configured timeout.</p>
     */
    @Observed(name = "intent.create", contextualName = "Internal intent creation")
    public IntentCreateResponse createIntent(@Valid IntentCreateRequest createRequest) {
        LocalDateTime createdAt = LocalDateTime.now();
        Intent intent = intentRepository.save(Intent.builder()
                .amount(createRequest.amount())
                .createdAt(createdAt)
                .expiresAt(createdAt.plus(configProps.timeout()))
                .build());
        IntentCreateResponse response = paymentMapper.toCreateResponse(intent);

        log.info("Internal intent created: {}.", response);

        return response;
    }

    /**
     * Submits an existing internal intent to the external payment gateway,
     * propagating its identifier as metadata to allow correlation with asynchronous webhook events.
     *
     * <p>The response includes gateway-specific information required by the client
     * to proceed with the payment.</p>
     */
    @Observed(name = "intent.submission", contextualName = "Internal intent submission")
    public IntentSubmissionResponse submitIntent(IntentSubmissionRequest request) {
        Intent intent = intentRepository
                .findById(request.intentId())
                .filter(i -> i.getExpiresAt().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new EntityNotFoundException(new EntityNotFoundError(
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
    @Observed(name = "intent.complete", contextualName = "Internal intent completion")
    public CompletionCreateResponse completeIntent(CompletionCreateRequest createRequest) {
        log.info("Completing intent with id {}.", createRequest.internalIntentId());

        Completion completion = completionRepository
                .findByIntentId(createRequest.internalIntentId())
                .orElse(completionRepository.save(Completion.builder()
                        .intent(Intent.builder()
                                .id(createRequest.internalIntentId())
                                .build())
                        .gatewayIntentId(createRequest.gatewayIntentId())
                        .createdAt(LocalDateTime.now())
                        .build()));
        CompletionCreateResponse response = paymentMapper.toCompletionCreateResponse(completion);

        log.info("Intent completion created: {}.", response);

        return response;
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
    public List<IntentCreateResponse> findExpiredIntents() {
        return intentRepository
                .findExpiredIntents().stream()
                .map(paymentMapper::toCreateResponse).toList();
    }

    public List<IntentGetResponse> findAllById(List<String> ids) {
        return StreamSupport.stream(
                intentRepository.findAllById(ids).spliterator(),
                false
        ).map(paymentMapper::toGetResponse).toList();
    }
}
