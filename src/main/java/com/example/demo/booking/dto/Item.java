package com.example.demo.booking.dto;

public record Item(
        String name,
        String quantity,
        Amount amount
) {
}
