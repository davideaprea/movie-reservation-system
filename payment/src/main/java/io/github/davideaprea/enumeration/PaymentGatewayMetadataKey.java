package io.github.davideaprea.enumeration;

import com.mrs.app.payment.entity.Intent;

/**
 * Enumeration of metadata keys used in interactions with the payment gateway.
 *
 * <p>These keys are attached as metadata to Stripe API requests (e.g. during
 * payment intent creation) in order to persist application-specific information.
 * The same metadata is later retrieved from Stripe webhook events, allowing
 * the application to correlate incoming events with internal domain entities.</p>
 *
 * <p>For example, {@link #INTENT_ID} can be used to associate a Stripe payment
 * with its corresponding internal payment or order identifier.</p>
 */
public enum PaymentGatewayMetadataKey {
    /**
     * Associates a Stripe intent with its corresponding internal entity {@link Intent#getId()}
     */
    INTENT_ID
}
