package com.mrs.app.hall.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "halls")
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(
            mappedBy = "hall",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Seat> seats;

    public void addSeat(Seat seat) {
        if (seats == null) {
            seats = new ArrayList<>();
        }

        seats.add(seat);
    }
}
