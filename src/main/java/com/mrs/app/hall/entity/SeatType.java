package com.mrs.app.hall.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "seat_types")
public class SeatType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String name;
}
