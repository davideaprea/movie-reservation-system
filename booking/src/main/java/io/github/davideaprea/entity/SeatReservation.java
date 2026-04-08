package io.github.davideaprea.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "seat_reservations")
public class SeatReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*The unique constraint prevents conflicting
    reservations for the same schedule seat*/
    @Column(nullable = false, unique = true)
    private Long scheduleSeatId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Booking booking;
}
