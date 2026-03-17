package com.mrs.app.payment.component;

import com.mrs.app.payment.dto.GatewayOrderCompletionResponse;
import com.mrs.app.payment.dto.GatewayOrderCreateRequest;
import com.mrs.app.payment.dto.GatewayOrderCreateResponse;
import com.mrs.app.payment.exception.PaymentGatewayException;
import com.mrs.app.payment.mapper.PayPalOrderMapper;
import com.mrs.app.payment.util.PayPalOrderUtils;
import com.paypal.sdk.PaypalServerSdkClient;
import com.paypal.sdk.models.CaptureOrderInput;
import com.paypal.sdk.models.Order;
import com.paypal.sdk.models.RefundCapturedPaymentInput;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PaymentGateway {
    private final PaypalServerSdkClient payPalClient;
    private final PayPalOrderMapper payPalOrderMapper;

    public GatewayOrderCreateResponse createOrder(GatewayOrderCreateRequest request) {
        Order order;

        try {
            order = payPalClient
                    .getOrdersController()
                    .createOrder(payPalOrderMapper.toCreateOrderInput(request))
                    .getResult();
        } catch (Exception e) {
            throw new PaymentGatewayException(e.getMessage());
        }

        return new GatewayOrderCreateResponse(order.getId());
    }

    public GatewayOrderCompletionResponse completeOrder(String id) {
        Order order;

        try {
            order = payPalClient
                    .getOrdersController()
                    .captureOrder(new CaptureOrderInput
                            .Builder()
                            .id(id)
                            .build())
                    .getResult();
        } catch (Exception e) {
            throw new PaymentGatewayException(e.getMessage());
        }

        return new GatewayOrderCompletionResponse(
                order.getId(),
                PayPalOrderUtils
                        .extractCaptureIdFromOrder(order)
                        .orElseThrow(() -> new IllegalStateException("The PayPal gateway hasn't returned the expected capture id."))
        );
    }

    public void refundOrder(String completionId) {
        try {
            payPalClient
                    .getPaymentsController()
                    .refundCapturedPayment(new RefundCapturedPaymentInput.Builder()
                            .captureId(completionId)
                            .build());
        } catch (Exception e) {
            throw new PaymentGatewayException(e.getMessage());
        }
    }
}
