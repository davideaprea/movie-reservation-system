package com.mrs.app.movie.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
