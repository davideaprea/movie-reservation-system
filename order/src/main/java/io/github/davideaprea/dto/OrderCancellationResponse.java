package io.github.davideaprea.dto;

import com.mrs.app.payment.dto.RefundResponse;

public record OrderCancellationResponse(
        long id,
        long userId,
        RefundResponse refund
) {
}
