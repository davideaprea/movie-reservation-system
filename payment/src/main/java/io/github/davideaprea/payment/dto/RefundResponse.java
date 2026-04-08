package io.github.davideaprea.payment.dto;

public record RefundResponse(
        long id,
        String gatewayId
) {
}
