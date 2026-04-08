package io.github.davideaprea.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Represents a seat for a specific schedule, including its pricing at the time of the screening.
 */
@Builder
@AllArgsConstructor
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "schedule_seats")
public class ScheduleSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * References the corresponding seat in the hall for this schedule.
     */
    @Column(nullable = false)
    private Long seatId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Schedule schedule;

    @Column(nullable = false)
    private BigDecimal price;
}
