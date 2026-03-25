package com.mrs.app.hall.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "seats", uniqueConstraints = {
        //Each seat's coordinates (rowNumber + seatNumber) must be unique within the same hall
        @UniqueConstraint(columnNames = {"hall_id", "row_number", "seat_number"})
})
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private SeatType type;

    @Column(nullable = false, updatable = false)
    private Integer rowNumber;

    @Column(nullable = false, updatable = false)
    private Integer seatNumber;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Hall hall;
}
