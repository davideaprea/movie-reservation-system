package com.example.demo.booking.entity;

import com.example.demo.cinema.entity.Schedule;
import com.example.demo.cinema.entity.Seat;
import com.example.demo.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

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
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Seat seat;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Schedule schedule;

    public static Booking create(
            long userId,
            long seatId,
            long scheduleId
    ) {
        return Booking
                .builder()
                .user(User.create(userId))
                .schedule(Schedule.create(scheduleId))
                .seat(Seat.create(seatId))
                .build();
    }
}
