package com.mrs.app.booking.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "bookings", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"schedule_id", "row_number", "seat_number"})
})
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long seatId;

    @Column(nullable = false)
    private Long scheduleId;

    @Column(nullable = false)
    private Long userId;
}
