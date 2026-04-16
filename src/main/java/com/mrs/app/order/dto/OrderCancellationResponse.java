package com.mrs.app.order.dto;

import com.mrs.app.payment.dto.RefundCreateResponse;

public record OrderCancellationResponse(
        long id,
        long userId,
        RefundCreateResponse refund
) {
}
