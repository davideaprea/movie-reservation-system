package com.example.demo.booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
    private Long id;

    private String orderId;

    private String captureId;

    private BigDecimal price;

    @OneToMany
    private List<Booking> items;
}
