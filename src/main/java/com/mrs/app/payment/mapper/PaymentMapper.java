package com.mrs.app.payment.mapper;

import com.mrs.app.payment.dto.PaymentResponse;
import com.mrs.app.payment.entity.Payment;
import com.mrs.app.payment.enumeration.PaymentStatus;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponse toResponse(Payment payment);

    default Payment toEntity(BigDecimal price, String gatewayOrderId) {
        return new Payment(
                null,
                new GatewayOrder(
                        gatewayOrderId,
                        null,
                        price
                ),
                LocalDateTime.now(),
                PaymentStatus.PENDING
        );
    }
}
