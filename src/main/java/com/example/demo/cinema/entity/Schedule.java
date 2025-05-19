package com.example.demo.cinema.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

@NoArgsConstructor
@FieldNameConstants
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@ToString
@Getter
@Entity
@Table(name = "schedules", indexes = {
        @Index(columnList = "hall_id, start_time, end_time"),
        @Index(columnList = "movie_id, start_time")
})
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Movie movie;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Hall hall;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    public static Schedule create(
            Movie movie,
            Hall hall,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        return Schedule
                .builder()
                .movie(movie)
                .hall(hall)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    public static Schedule create(long id) {
        return Schedule
                .builder()
                .id(id)
                .build();
    }
}
