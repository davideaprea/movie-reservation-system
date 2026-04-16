package com.mrs.app.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represent a {@link Completion} refund.
 */
@Builder
@AllArgsConstructor
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refunds")
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(unique = true)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Completion completion;

    @Column(unique = true, nullable = false)
    private String gatewayRefundId;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
