package com.mrs.app.payment.mapper;

import com.mrs.app.payment.dto.PaymentCreateRequest;
import com.mrs.app.payment.dto.PaymentResponse;
import com.mrs.app.payment.entity.GatewayOrder;
import com.mrs.app.payment.entity.Payment;
import com.mrs.app.payment.enumeration.PaymentStatus;
import com.paypal.sdk.models.Order;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

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
                createRequest.userId(),
                LocalDateTime.now(),
                PaymentStatus.PENDING
        );
    }
}
