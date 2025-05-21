package com.example.demo.booking.dto;

import com.example.demo.booking.enumeration.PayPalOrderIntent;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public record OrderDto(
        PayPalOrderIntent intent,
        @JsonProperty("purchase_units")
        List<PurchaseUnit> purchaseUnits
) {
    public OrderDto(BigDecimal totalPrice) {
        this(
                PayPalOrderIntent.CAPTURE,
                List.of(new PurchaseUnit(
                        new Amount("EUR", String.valueOf(totalPrice))
                ))
        );
    }
}
