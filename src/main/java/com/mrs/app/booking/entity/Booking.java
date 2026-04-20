package com.mrs.app.booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a group of {@link SeatReservation} for a specific schedule.
 * <p>
 * This entity facilitates retrieving reservations by {@link #scheduleId} and
 * is also used to create bookings transactional-ly: either all seats in the group
 * are reserved successfully, or none are saved if any seat is already booked.
 */
@Builder
@AllArgsConstructor
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(
            mappedBy = "booking",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private List<SeatReservation> seatReservations;

    @Column(nullable = false, updatable = false)
    private Long scheduleId;

    public void addSeatReservation(SeatReservation seatReservation) {
        if (seatReservations == null) {
            seatReservations = new ArrayList<>();
        }

        seatReservations.add(seatReservation);
    }
}
