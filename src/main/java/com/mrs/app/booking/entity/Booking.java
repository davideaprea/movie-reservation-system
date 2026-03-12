package com.mrs.app.booking.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long scheduleSeatId;

    @Column(nullable = false)
    private Long userId;
}
