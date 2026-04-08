package io.github.davideaprea.order.dto;

import com.mrs.app.payment.dto.RefundResponse;

public record OrderCancellationResponse(
        long id,
        long userId,
        RefundResponse refund
) {
}
