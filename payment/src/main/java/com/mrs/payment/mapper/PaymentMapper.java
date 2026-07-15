package com.mrs.payment.mapper;

import com.mrs.payment.dto.CompletionCreateResponse;
import com.mrs.payment.dto.IntentCreateResponse;
import com.mrs.payment.dto.IntentGetResponse;
import com.mrs.payment.entity.Completion;
import com.mrs.payment.entity.Intent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    IntentCreateResponse toCreateResponse(Intent intent);

    CompletionCreateResponse toCompletionCreateResponse(Completion completion);

    IntentGetResponse toGetResponse(Intent intent);
}
