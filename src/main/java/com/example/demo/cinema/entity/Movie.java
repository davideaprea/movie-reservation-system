package com.example.demo.cinema.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@ToString
@Getter
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String cover;

    @OneToMany(mappedBy = Schedule.Fields.movie, fetch = FetchType.LAZY)
    private List<Schedule> schedules;

    public static Movie createWithId(long id) {
        return Movie
                .builder()
                .id(id)
                .build();
    }

    public static Movie create(
            String title,
            int duration,
            String description,
            String cover
    ) {
        return Movie
                .builder()
                .title(title)
                .duration(duration)
                .description(description)
                .cover(cover)
                .build();
    }
}
