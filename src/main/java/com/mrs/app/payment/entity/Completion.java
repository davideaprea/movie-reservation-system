package com.mrs.app.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Entity
@Table(name = "completions")
public class Completion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(unique = true)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Intent intent;

    @Column(nullable = false, unique = true)
    private String gatewayIntentId;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
