package com.mrs.app.booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
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
