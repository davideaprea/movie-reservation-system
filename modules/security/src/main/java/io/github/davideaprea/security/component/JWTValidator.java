package io.github.davideaprea.security.component;

import io.jsonwebtoken.JwtParser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JWTValidator {
    private final JwtParser jwtParser;

    public String extractSubject(final String token) {
        return jwtParser
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
