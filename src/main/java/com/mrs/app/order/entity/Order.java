package com.mrs.app.order.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents an order linking a booking to its payment,
 * associating it with the customer who created the request.
 * <p>
 * This entity allows tracking the payment status of a booking
 * and is essential for completing and refunding payments.
 */
@Builder
@Getter
@AllArgsConstructor
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long intentId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long bookingId;
}
