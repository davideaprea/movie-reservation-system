package com.mrs.app.payment.component;

import com.mrs.app.payment.dto.PaymentGatewayOrderRequest;
import com.mrs.app.payment.exception.PaymentGatewayException;
import com.mrs.app.payment.mapper.PayPalOrderMapper;
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

    public Order createOrder(PaymentGatewayOrderRequest request) {
        try {
            return payPalClient
                    .getOrdersController()
                    .createOrder(payPalOrderMapper.toCreateOrderInput(request))
                    .getResult();
        } catch (Exception e) {
            throw new PaymentGatewayException(e.getMessage());
        }
    }

    public Order completeOrder(String id) {
        try {
            return payPalClient
                    .getOrdersController()
                    .captureOrder(new CaptureOrderInput
                            .Builder()
                            .id(id)
                            .build())
                    .getResult();
        } catch (Exception e) {
            throw new PaymentGatewayException(e.getMessage());
        }
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
