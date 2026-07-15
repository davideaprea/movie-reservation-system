package com.mrs.order.dto;

public record OrderCancellationRequest(
        long userId,
        long orderId
) {
}
