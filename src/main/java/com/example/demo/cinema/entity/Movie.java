package com.example.demo.cinema.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

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
    private Short duration;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String cover;

    public static Movie create(long id) {
        return Movie
                .builder()
                .id(id)
                .build();
    }

    public static Movie create(
            String title,
            short duration,
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
