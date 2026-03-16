package com.mrs.app.order.dto;

public record OrderUpdateRequest(
        long userId,
        long orderId
) {
}
