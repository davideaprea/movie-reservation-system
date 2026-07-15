package com.mrs.payment.dto.gateway;

public record GatewayIntentCreateResponse(
        String id,
        String clientSecret,
        String nextRequiredStep
) {
}
