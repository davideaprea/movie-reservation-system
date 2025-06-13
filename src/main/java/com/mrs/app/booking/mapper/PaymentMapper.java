package com.mrs.app.booking.mapper;

import com.mrs.app.booking.dto.projection.PaymentProjection;
import com.mrs.app.booking.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mapping(source = "user.id", target = "userId")
    PaymentProjection entityToProjection(Payment payment);
}
