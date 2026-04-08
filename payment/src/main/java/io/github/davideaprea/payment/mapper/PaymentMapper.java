package io.github.davideaprea.payment.mapper;

import io.github.davideaprea.payment.dto.CompletionCreateResponse;
import io.github.davideaprea.payment.dto.IntentResponse;
import io.github.davideaprea.payment.entity.Completion;
import io.github.davideaprea.payment.entity.Intent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    IntentResponse toResponse(Intent intent);

    CompletionCreateResponse toCompletionCreateResponse(Completion completion);
}
