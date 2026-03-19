package com.mrs.app.payment.mapper;

import com.mrs.app.payment.dto.PaymentResponse;
import com.mrs.app.payment.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponse toResponse(Payment payment);
}
