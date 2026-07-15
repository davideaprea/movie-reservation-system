package com.mrs.order.dto;

import com.mrs.payment.dto.RefundResponse;

public record OrderCancellationResponse(
        long id,
        long userId,
        RefundResponse refund
) {
}
