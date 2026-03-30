package com.mrs.app.payment.dto.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "stripe",
        ignoreUnknownFields = false
)
public record StripeClientConfigProps(
        String apiKey
) {
}
