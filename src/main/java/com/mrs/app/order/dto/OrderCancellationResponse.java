package com.mrs.app.order.dto;

import com.mrs.app.payment.dto.RefundResponse;

public record OrderCancellationResponse(
        long id,
        long userId,
        RefundResponse refund
) {
}
