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
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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

    public IntentCreateResponse createIntent(@Valid IntentCreateRequest createRequest) {
        Intent intent;

        try {
            LocalDateTime createdAt = LocalDateTime.now();
            intent = intentDAO.save(Intent.builder()
                    .orderId(createRequest.orderId())
                    .amount(createRequest.amount())
                    .createdAt(createdAt)
                    .expiresAt(createdAt.plusMinutes(15))
                    .build());
        } catch (DataIntegrityViolationException e) {
            intent = intentDAO
                    .findByOrderId(createRequest.orderId())
                    .orElseThrow();
        }

        GatewayIntentCreateResponse gatewayIntent = paymentGateway.createIntent(new GatewayIntentCreateRequest(
                intent.getAmount(),
                "EUR",
                intent.getOrderId()
        ));

        return new IntentCreateResponse(
                gatewayIntent,
                paymentMapper.toResponse(intent)
        );
    }

    public CompletionCreateResponse completeIntent(CompletionCreateRequest createRequest) {
        Intent intent = intentDAO
                .findByOrderId(createRequest.orderId())
                .orElseThrow();
        Completion completion;

        try {
            completion = completionDAO.save(Completion.builder()
                    .intent(intent)
                    .gatewayIntentId(createRequest.intentId())
                    .createdAt(LocalDateTime.now())
                    .build());
        } catch (DataIntegrityViolationException e) {
            completion = completionDAO
                    .findByIntentId(intent.getId())
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
