package com.mrs.app.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Embeddable
public class GatewayOrder {
    @Column(unique = true, nullable = false, updatable = false)
    private String id;

    @Setter
    @Column(unique = true)
    private String completionId;

    @Column(nullable = false)
    private BigDecimal price;
}
