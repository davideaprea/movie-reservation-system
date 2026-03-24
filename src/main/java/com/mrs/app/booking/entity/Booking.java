package com.mrs.app.booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
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
