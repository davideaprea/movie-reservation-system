package com.mrs.app.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Represent a payment completion,
 * marking the related {@link Intent} as completed.
 */
@Builder
@AllArgsConstructor
@Getter
@Entity
@Table(name = "completions")
public class Completion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JoinColumn(unique = true)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Intent intent;

    @Column(nullable = false, unique = true)
    private String gatewayIntentId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "completion")
    private Refund refund;
}
