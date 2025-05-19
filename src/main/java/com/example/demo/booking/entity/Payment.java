package com.example.demo.booking.entity;

import com.example.demo.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Entity
@Table(name = "payments")
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

    @OneToMany(
            mappedBy = Booking.Fields.payment,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Booking> items;

    public static Payment create(
            String orderId,
            BigDecimal price,
            User user
    ) {
        return Payment
                .builder()
                .orderId(orderId)
                .price(price)
                .user(user)
                .build();
    }

    public static Payment create(long id) {
        return Payment
                .builder()
                .id(id)
                .build();
    }
}
