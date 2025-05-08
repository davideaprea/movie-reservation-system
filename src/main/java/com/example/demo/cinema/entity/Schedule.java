package com.example.demo.cinema.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
@ToString
@Getter
@Entity
@Table(name = "schedules")
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
            long movieId,
            long hallId,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        return Schedule
                .builder()
                .movie(Movie.create(movieId))
                .hall(Hall.create(hallId))
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
