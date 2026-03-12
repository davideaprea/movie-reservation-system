package com.mrs.app.security.dto;

import java.util.List;

public record JWTClaims(
        String subject,
        List<String> roles
) {
}
