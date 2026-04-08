package io.github.davideaprea.dto.gateway;

public record GatewayIntentCreateResponse(
        String id,
        String clientSecret,
        String nextRequiredStep
) {
}
