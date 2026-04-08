package io.github.davideaprea.order.dto;

import io.github.davideaprea.payment.dto.RefundResponse;

public record OrderCancellationResponse(
        long id,
        long userId,
        RefundResponse refund
) {
}
