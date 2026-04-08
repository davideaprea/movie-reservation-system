package io.github.davideaprea.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents an order linking a booking to its intent,
 * associating it with the customer who created the request.
 * <p>
 * This entity allows tracking the intent status of a booking
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
    private Long userId;

    @Column(nullable = false, unique = true)
    private Long bookingId;

    @Column(nullable = false, unique = true)
    private String intentId;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
