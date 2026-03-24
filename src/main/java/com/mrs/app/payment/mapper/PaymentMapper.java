package com.mrs.app.payment.mapper;

import com.mrs.app.payment.dto.IntentResponse;
import com.mrs.app.payment.entity.Intent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    IntentResponse toResponse(Intent intent);
}
