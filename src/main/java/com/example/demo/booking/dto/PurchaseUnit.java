package com.example.demo.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PurchaseUnit(
        Amount amount,
        List<Item> items,
        @JsonProperty("custom_id")
        String customId
) {
}
