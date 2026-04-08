package io.github.davideaprea.order.dto;

public record OrderCancellationRequest(
        long userId,
        long orderId
) {
}
