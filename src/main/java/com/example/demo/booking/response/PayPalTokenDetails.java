package com.example.demo.booking.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PayPalTokenDetails(
        String scope,
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("app_id") String appId,
        @JsonProperty("expires_in") int expiresIn,
        String nonce
) {
}
