package com.example.demo.booking.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PayPalOrder(
        String id
) {
}
