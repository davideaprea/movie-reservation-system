package com.mrs.app.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Represents a pending payment request.
 */
@Builder
@AllArgsConstructor
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Retrieved from the payment gateway after creating the intent.
     * Stored to allow completing the payment later through the gateway.
     */
    @Setter
    @Column(unique = true)
    private String gatewayPaymentId;

    @Column(unique = true, nullable = false)
    private String gatewayIdempotencyKey;

    @Column(nullable = false)
    private BigDecimal price;
}
