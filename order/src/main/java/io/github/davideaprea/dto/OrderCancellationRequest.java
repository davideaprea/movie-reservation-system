package io.github.davideaprea.dto;

public record OrderCancellationRequest(
        long userId,
        long orderId
) {
}
