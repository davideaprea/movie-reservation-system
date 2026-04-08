package io.github.davideaprea.payment.mapper;

import com.mrs.app.payment.dto.CompletionCreateResponse;
import com.mrs.app.payment.dto.IntentResponse;
import com.mrs.app.payment.entity.Completion;
import com.mrs.app.payment.entity.Intent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    IntentResponse toResponse(Intent intent);

    CompletionCreateResponse toCompletionCreateResponse(Completion completion);
}
