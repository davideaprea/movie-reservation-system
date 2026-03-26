package com.mrs.app.movie.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private String coverImageLink;

    @JoinTable(
            name = "movies_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Genre> genres;
}
