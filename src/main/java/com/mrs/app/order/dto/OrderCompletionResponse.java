package com.mrs.app.order.dto;

import com.mrs.app.payment.dto.CompletionResponse;

public record OrderCompletionResponse(
        long id,
        long userId,
        CompletionResponse completion
) {
}
