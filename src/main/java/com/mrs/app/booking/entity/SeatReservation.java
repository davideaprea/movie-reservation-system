package com.mrs.app.booking.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "seat_reservations")
public class SeatReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long scheduleSeatId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Booking booking;
}
