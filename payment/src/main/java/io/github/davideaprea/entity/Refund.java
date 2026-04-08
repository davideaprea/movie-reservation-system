package io.github.davideaprea.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Completion completion;

    @Column(unique = true, nullable = false)
    private String gatewayRefundId;
}
