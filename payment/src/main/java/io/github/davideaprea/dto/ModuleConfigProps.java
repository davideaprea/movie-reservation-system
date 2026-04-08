package io.github.davideaprea.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.payment")
public record ModuleConfigProps(
        Duration timeout
) {
}
