package com.mrs.app.payment.service;

import com.mrs.app.payment.component.PaymentGateway;
import com.mrs.app.payment.dto.*;
import com.mrs.app.payment.dto.gateway.GatewayIntentCreateRequest;
import com.mrs.app.payment.dto.gateway.GatewayIntentCreateResponse;
import com.mrs.app.payment.entity.Completion;
import com.mrs.app.payment.entity.Intent;
import com.mrs.app.payment.mapper.PaymentMapper;
import com.mrs.app.payment.repository.CompletionDAO;
import com.mrs.app.payment.repository.IntentDAO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentGateway paymentGateway;
    private final IntentDAO intentDAO;
    private final CompletionDAO completionDAO;
    private final PaymentMapper paymentMapper;
    private final Duration paymentTimeout;

    public PaymentService(PaymentGateway paymentGateway, IntentDAO intentDAO, CompletionDAO completionDAO, PaymentMapper paymentMapper, @Value("${app.payment.timeout}") Duration paymentTimeout) {
        this.paymentGateway = paymentGateway;
        this.intentDAO = intentDAO;
        this.completionDAO = completionDAO;
        this.paymentMapper = paymentMapper;
        this.paymentTimeout = paymentTimeout;
    }

    public IntentResponse createIntent(@Valid IntentCreateRequest createRequest) {
        LocalDateTime createdAt = LocalDateTime.now();
        Intent intent = intentDAO.save(Intent.builder()
                .amount(createRequest.amount())
                .createdAt(createdAt)
                .expiresAt(createdAt.plus(paymentTimeout))
                .build());

        return paymentMapper.toResponse(intent);
    }

    public IntentSubmissionResponse submitIntent(IntentSubmissionRequest request) {
        Intent intent = intentDAO
                .findById(request.intentId())
                .orElseThrow();
        GatewayIntentCreateResponse response = paymentGateway.createIntent(new GatewayIntentCreateRequest(
                intent.getAmount(),
                "EUR",
                request.intentId()
        ));

        return new IntentSubmissionResponse(response.id(), response.nextRequiredStep(), response.clientSecret());
    }

    public CompletionCreateResponse completeIntent(CompletionCreateRequest createRequest) {
        Completion completion;

        try {
            completion = completionDAO.save(Completion.builder()
                    .intent(Intent.builder()
                            .id(createRequest.internalIntentId())
                            .build())
                    .gatewayIntentId(createRequest.gatewayIntentId())
                    .createdAt(LocalDateTime.now())
                    .build());
        } catch (DataIntegrityViolationException e) {
            completion = completionDAO
                    .findByIntentId(createRequest.internalIntentId())
                    .orElseThrow();
        }

        return paymentMapper.toCompletionCreateResponse(completion);
    }

    public List<IntentResponse> findAllExpired() {
        return intentDAO
                .findAllExpired().stream()
                .map(paymentMapper::toResponse).toList();
    }
}
