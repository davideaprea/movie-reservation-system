package com.example.demo.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfig {
    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> pgContainer() {
        final DockerImageName imageName = DockerImageName.parse("postgres:17-alpine");

        return new PostgreSQLContainer<>(imageName).withDatabaseName("cinema");
    }
}
