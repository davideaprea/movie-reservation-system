package com.mrs.app.shared.paypal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PayPalOrder(
        String id
) {
}
