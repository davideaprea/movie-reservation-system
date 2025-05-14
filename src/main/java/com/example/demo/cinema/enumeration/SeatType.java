package com.example.demo.cinema.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SeatType {
    REGULAR(5),
    VIP(7);

    private final float price;
}
