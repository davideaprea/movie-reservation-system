package com.mrs.app.shared.paypal.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "paypal")
public record PaymentGatewayProps(
        String baseUrl,
        String clientId,
        String secret
) {
}
