package com.mrs.app.payment.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represent a payment completion,
 * marking the related {@link Intent} as completed.
 */
@Builder
@AllArgsConstructor
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "completions")
public class Completion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(unique = true)
    private Intent intent;

    /**
     * Retrieved from the payment gateway after completing the intent.
     * Stored to allow possible refunds through the gateway.
     */
    @Setter
    @Column(unique = true)
    private String gatewayCompletionId;
}
