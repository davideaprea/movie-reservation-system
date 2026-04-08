package io.github.davideaprea.security.dto;

import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;
import java.util.Base64;

@ConfigurationProperties(prefix = "jwt")
public record JWTConfigProps(
        String secret,
        long expTime
) {
    public SecretKey key() {
        return Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(secret)
        );
    }
}
