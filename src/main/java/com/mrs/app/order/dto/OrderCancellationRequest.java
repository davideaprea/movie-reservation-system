package com.mrs.app.order.dto;

public record OrderCancellationRequest(
        long userId,
        long orderId
) {
}
