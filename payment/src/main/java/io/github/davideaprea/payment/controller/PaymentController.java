package io.github.davideaprea.payment.controller;

import com.mrs.app.payment.dto.CompletionCreateRequest;
import com.mrs.app.payment.enumeration.PaymentGatewayMetadataKey;
import com.mrs.app.payment.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final String endpointSecret;

    public PaymentController(PaymentService paymentService, @Value("${stripe.webhook-invoked-api-secret}") String endpointSecret) {
        this.paymentService = paymentService;
        this.endpointSecret = endpointSecret;
    }

    @PostMapping("intents")
    public ResponseEntity<Void> completeIntent(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature
    ) {
        Event event;

        try {
            event = Webhook.constructEvent(payload, signature, endpointSecret);
        } catch (SignatureVerificationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();
        String intentId = intent.getMetadata().get(PaymentGatewayMetadataKey.INTENT_ID.name());

        paymentService.completeIntent(new CompletionCreateRequest(intent.getId(), intentId));

        return ResponseEntity.noContent().build();
    }
}
