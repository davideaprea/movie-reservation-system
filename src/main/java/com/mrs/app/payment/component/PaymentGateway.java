package com.mrs.app.payment.component;

import com.mrs.app.payment.dto.gateway.GatewayIntentCreateRequest;
import com.mrs.app.payment.dto.gateway.GatewayIntentCreateResponse;
import com.mrs.app.payment.dto.gateway.GatewayRefundResponse;
import com.mrs.app.payment.enumeration.PaymentGatewayMetadataKey;
import com.mrs.app.payment.exception.PaymentGatewayException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.net.RequestOptions.RequestOptionsBuilder;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Handles interactions with the external intent gateway,
 * providing abstractions over the service payloads and exceptions.
 */
@Slf4j
@AllArgsConstructor
@Component
public class PaymentGateway {
    private final RequestOptionsBuilder baseRequestOptionsBuilder;

    /**
     * Creates a new intent for the specified amount and details.
     * <p>
     * The returned {@link GatewayIntentCreateResponse#id()} should be stored to allow payment completion.
     */
    @Observed(name = "payment-gateway.intent.create", contextualName = "Payment gateway intent creation")
    public GatewayIntentCreateResponse createIntent(GatewayIntentCreateRequest request) {
        log.info("Creating an intent via the payment gateway with params {}.", request);

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

        log.info("""
                The payment gateway has successfully created
                the intent with id {} and status {}.
                """, intent.getId(), intent.getStatus());

        return new GatewayIntentCreateResponse(
                intent.getId(),
                intent.getClientSecret(),
                intent.getStatus()
        );
    }

    public GatewayRefundResponse refund(String paymentId) {
        try {
            Refund refund = Refund.create(RefundCreateParams.builder()
                            .setPaymentIntent(paymentId)
                            .build(),
                    baseRequestOptionsBuilder.build());

            return new GatewayRefundResponse(refund.getId());
        } catch (Exception e) {
            throw new PaymentGatewayException(e.getMessage());
        }
    }
}
