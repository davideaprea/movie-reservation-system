package io.github.davideaprea.security.dto;

import java.util.List;

public record JWTClaims(
        String subject,
        List<String> roles
) {
}
