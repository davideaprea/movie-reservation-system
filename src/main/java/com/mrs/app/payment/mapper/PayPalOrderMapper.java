package com.mrs.app.payment.mapper;

import com.mrs.app.payment.dto.PaymentCreateRequest;
import com.paypal.sdk.models.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PayPalOrderMapper {
    public CreateOrderInput toCreateOrderInput(PaymentCreateRequest createRequest) {
        OrderRequest paypalOrder = new OrderRequest
                .Builder()
                .intent(CheckoutPaymentIntent.CAPTURE)
                .purchaseUnits(List.of(new PurchaseUnitRequest
                        .Builder()
                        .amount(new AmountWithBreakdown
                                .Builder()
                                .currencyCode("EUR")
                                .value(createRequest.totalPrice().toString())
                                .build())
                        .build()))
                .build();

        return new CreateOrderInput
                .Builder()
                .body(paypalOrder)
                .build();
    }
}
