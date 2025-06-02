package com.mrs.app.booking.dto.internal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PayPalCapturedOrder(
        @JsonProperty("purchase_units")
        List<PurchaseUnit> purchaseUnits
) {
    public record PurchaseUnit(
            Payments payments
    ) {
    }

    public record Payments(
            List<Capture> captures
    ) {
    }

    public record Capture(
            String id
    ) {
    }
}

