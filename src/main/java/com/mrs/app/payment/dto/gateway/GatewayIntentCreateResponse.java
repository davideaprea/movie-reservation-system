package com.mrs.app.payment.dto.gateway;

public record GatewayIntentCreateResponse(
        String id,
        String clientSecret,
        String nextRequiredStep
) {
}
