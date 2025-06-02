package com.mrs.app.booking.entity;

import com.mrs.app.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Entity
@Table(name = "payments", indexes = {
        @Index(columnList = "capture_id, created_at")
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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = Booking.Fields.payment,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Booking> items;

    public static Payment create(
            String orderId,
            BigDecimal price,
            long userId
    ) {
        return Payment
                .builder()
                .orderId(orderId)
                .price(price)
                .user(User.createWithId(userId))
                .build();
    }

    public static Payment createWithId(long id) {
        return Payment
                .builder()
                .id(id)
                .build();
    }
}
