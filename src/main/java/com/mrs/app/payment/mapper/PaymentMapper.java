package com.mrs.app.payment.mapper;

import com.mrs.app.payment.dto.PaymentCreateRequest;
import com.mrs.app.payment.dto.PaymentResponse;
import com.mrs.app.payment.entity.GatewayOrder;
import com.mrs.app.payment.entity.Payment;
import com.mrs.app.payment.enumeration.PaymentStatus;
import com.paypal.sdk.models.*;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponse toResponse(Payment payment);

    default Payment toEntity(
            PaymentCreateRequest createRequest,
            Order payPalOrder
    ) {
        return new Payment(
                null,
                new GatewayOrder(
                        payPalOrder.getId(),
                        null,
                        createRequest.totalPrice()
                ),
                LocalDateTime.now(),
                PaymentStatus.PENDING
        );
    }

    default CreateOrderInput toCreateOrderInput(PaymentCreateRequest createRequest) {
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
