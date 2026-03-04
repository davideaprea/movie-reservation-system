package com.mrs.app.payment.mapper;

import com.mrs.app.payment.dto.PaymentProjection;
import com.mrs.app.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mapping(source = "user.id", target = "userId")
    PaymentProjection entityToProjection(Payment payment);
}
