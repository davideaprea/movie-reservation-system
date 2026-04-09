package com.mrs.app.payment.mapper;

import com.mrs.app.payment.dto.CompletionCreateResponse;
import com.mrs.app.payment.dto.IntentCreateResponse;
import com.mrs.app.payment.dto.IntentGetResponse;
import com.mrs.app.payment.entity.Completion;
import com.mrs.app.payment.entity.Intent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    IntentCreateResponse toCreateResponse(Intent intent);

    CompletionCreateResponse toCompletionCreateResponse(Completion completion);

    IntentGetResponse toGetResponse(Intent intent);
}
