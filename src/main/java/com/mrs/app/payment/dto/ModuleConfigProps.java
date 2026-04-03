package com.mrs.app.payment.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.payment")
public record ModuleConfigProps(
        Duration timeout
) {
}
