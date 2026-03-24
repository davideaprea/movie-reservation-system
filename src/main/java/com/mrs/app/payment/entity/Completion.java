package com.mrs.app.payment.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne
    @JoinColumn(unique = true)
    private Intent intent;

    @Setter
    @Column(unique = true)
    private String gatewayCompletionId;
}
