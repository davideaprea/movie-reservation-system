package io.github.davideaprea.dto;

import java.util.List;

public record JWTClaims(
        String subject,
        List<String> roles
) {
}
