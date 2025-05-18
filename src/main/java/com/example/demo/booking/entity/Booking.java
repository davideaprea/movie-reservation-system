package com.example.demo.booking.entity;

import com.example.demo.cinema.entity.Schedule;
import com.example.demo.cinema.entity.Seat;
import com.example.demo.security.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "bookings", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"seat_id", "schedule_id"})
})
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Seat seat;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Schedule schedule;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Payment payment;

    public static Booking create(
            long paymentId,
            long seatId,
            long scheduleId
    ) {
        return Booking
                .builder()
                .schedule(Schedule.create(scheduleId))
                .seat(Seat.create(seatId))
                .payment(Payment.create(paymentId))
                .build();
    }
}
