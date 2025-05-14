package com.example.demo.booking.entity;

import com.example.demo.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
    private Long id;

    private String orderId;

    private String captureId;

    private BigDecimal price;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @OneToMany
    private List<Booking> items;

    public static Payment create(
            String orderId,
            BigDecimal price,
            List<Booking> items,
            long userId
    ) {
        return Payment
                .builder()
                .orderId(orderId)
                .price(price)
                .items(items)
                .user(User.create(userId))
                .build();
    }
}
