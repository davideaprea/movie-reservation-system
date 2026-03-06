package com.mrs.app.payment.dto;

import com.paypal.sdk.Environment;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "paypal",
        ignoreUnknownFields = false
)
public record PayPalClientConfigProps(
        Environment environment,
        String clientId,
        String clientSecret
) {
}
