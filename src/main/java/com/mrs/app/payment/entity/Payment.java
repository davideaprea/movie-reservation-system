package com.mrs.app.payment.entity;

import com.mrs.app.booking.entity.Booking;
import com.mrs.app.payment.enumeration.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Entity
@Table(name = "payments", indexes = {
        @Index(columnList = "status, created_at")
})
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String orderId;

    @Column(unique = true)
    private String captureId;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false, updatable = false)
    private Long userId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = Booking.Fields.payment,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Booking> items;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
