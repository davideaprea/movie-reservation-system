package com.example.demo.booking.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PayPalTokenDetails(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("expires_in") int expiresIn
) {
}
