package com.mrs.app.payment.component;

import com.mrs.app.payment.dto.gateway.GatewayIntentCreateRequest;
import com.mrs.app.payment.dto.gateway.GatewayIntentCreateResponse;
import com.mrs.app.payment.dto.gateway.GatewayRefundCreateRequest;
import com.mrs.app.payment.dto.gateway.GatewayRefundResponse;
import com.mrs.app.payment.enumeration.PaymentGatewayMetadataKey;
import com.mrs.app.payment.exception.PaymentGatewayException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.net.RequestOptions.RequestOptionsBuilder;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Handles interactions with the external intent gateway,
 * providing abstractions over the service payloads and exceptions.
 */
@AllArgsConstructor
@Component
public class PaymentGateway {
    private final RequestOptionsBuilder baseRequestOptionsBuilder;

    /**
     * Creates a new intent for the specified amount and details.
     * <p>
     * The returned {@link GatewayIntentCreateResponse#id()} should be stored to allow payment completion.
     */
    public GatewayIntentCreateResponse createIntent(GatewayIntentCreateRequest request) {
        PaymentIntent intent;

        try {
            intent = PaymentIntent.create(
                    PaymentIntentCreateParams.builder()
                            .setAmount(request.price().longValue())
                            .setCurrency("EUR")
                            .setConfirm(true)
                            .putMetadata(PaymentGatewayMetadataKey.INTENT_ID.name(), request.key())
                            .build(),
                    baseRequestOptionsBuilder
                            .setIdempotencyKey(request.key())
                            .build()
            );
        } catch (Exception e) {
            throw new PaymentGatewayException(e.getMessage());
        }

        return new GatewayIntentCreateResponse(
                intent.getId(),
                intent.getClientSecret(),
                intent.getStatus()
        );
    }

    public GatewayRefundResponse refund(GatewayRefundCreateRequest request) {
        try {
            Refund refund = Refund.create(RefundCreateParams.builder()
                            .setPaymentIntent(request.gatewayCompletionId())
                            .putMetadata(PaymentGatewayMetadataKey.COMPLETION_ID.name(), request.internalCompletionId())
                            .build(),
                    baseRequestOptionsBuilder
                            .setIdempotencyKey(request.internalCompletionId())
                            .build());

            return new GatewayRefundResponse(refund.getId());
        } catch (Exception e) {
            throw new PaymentGatewayException(e.getMessage());
        }
    }
}
