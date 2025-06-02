package com.example.demo.booking.dto.internal;

import com.example.demo.booking.enumeration.PayPalOrderIntent;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public record PayPalOrderDto(
        PayPalOrderIntent intent,
        @JsonProperty("purchase_units")
        List<PurchaseUnit> purchaseUnits
) {
    public PayPalOrderDto(BigDecimal totalPrice) {
        this(
                PayPalOrderIntent.CAPTURE,
                List.of(new PurchaseUnit(
                        new Amount("EUR", String.valueOf(totalPrice))
                ))
        );
    }

    public record PurchaseUnit(
            Amount amount
    ) {
    }

    public record Amount(
            @JsonProperty("currency_code")
            String currencyCode,
            String value
    ) {
    }
}

