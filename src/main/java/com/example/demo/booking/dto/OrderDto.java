package com.example.demo.booking.dto;

import com.example.demo.booking.enumeration.PayPalOrderIntent;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OrderDto(
        PayPalOrderIntent intent,
        @JsonProperty("purchase_units")
        List<PurchaseUnit> purchaseUnits
) {
}
