package com.mrs.app.payment.mapper;

import com.mrs.app.payment.dto.gateway.GatewayPaymentCreateRequest;
import com.paypal.sdk.models.*;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PayPalOrderMapper {
    default CreateOrderInput toCreateOrderInput(GatewayPaymentCreateRequest request) {
        OrderRequest paypalOrder = new OrderRequest
                .Builder()
                .intent(CheckoutPaymentIntent.CAPTURE)
                .purchaseUnits(List.of(new PurchaseUnitRequest
                        .Builder()
                        .amount(new AmountWithBreakdown
                                .Builder()
                                .currencyCode(request.currencyCode())
                                .value(request.price().toString())
                                .build())
                        .build()))
                .build();

        return new CreateOrderInput
                .Builder()
                .body(paypalOrder)
                .build();
    }
}
