package com.mrs.app.booking.entity;

import com.mrs.app.cinema.entity.Schedule;
import com.mrs.app.cinema.entity.Seat;
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
        @UniqueConstraint(columnNames = {"schedule_id", "seat_id"})
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
                .schedule(Schedule.createWithId(scheduleId))
                .seat(Seat.createWithId(seatId))
                .payment(Payment.createWithId(paymentId))
                .build();
    }
}
