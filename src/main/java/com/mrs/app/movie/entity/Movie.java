package com.mrs.app.movie.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
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
    private Duration duration;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String cover;

    @ManyToMany
    private List<Genre> genres;
}
