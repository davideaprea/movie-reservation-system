package com.mrs.app.payment.component;

import com.mrs.app.payment.dto.gateway.GatewayPaymentCreateRequest;
import com.mrs.app.payment.dto.gateway.GatewayPaymentCreateResponse;
import com.mrs.app.payment.dto.gateway.GatewayRefundResponse;
import com.mrs.app.payment.exception.PaymentGatewayException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.net.RequestOptions.RequestOptionsBuilder;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Handles interactions with the external payment gateway,
 * providing abstractions over the service payloads and exceptions.
 */
@AllArgsConstructor
@Component
public class PaymentGateway {
    private final RequestOptionsBuilder baseRequestOptionsBuilder;

    /**
     * Creates a new payment for the specified amount and details.
     * <p>
     * The returned {@link GatewayPaymentCreateResponse#id()} should be stored to allow possible refunds.
     */
    public GatewayPaymentCreateResponse pay(GatewayPaymentCreateRequest request) {
        PaymentIntent intent;

        try {
            intent = PaymentIntent.create(
                    PaymentIntentCreateParams.builder()
                            .setAmount(request.price().longValue())
                            .setCurrency("EUR")
                            .setConfirm(true)
                            .build(),
                    baseRequestOptionsBuilder
                            .setIdempotencyKey(request.key())
                            .build()
            );
        } catch (Exception e) {
            throw new PaymentGatewayException(e.getMessage());
        }

        return new GatewayPaymentCreateResponse(intent.getId());
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
