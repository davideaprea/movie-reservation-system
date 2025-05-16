package com.example.demo.cinema.entity;

import com.example.demo.cinema.enumeration.HallStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@ToString
@Getter
@Entity
@Table(name = "halls")
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HallStatus hallStatus;

    @OneToMany(
            mappedBy = Seat.Fields.hall,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<Seat> seats;

    public static Hall create(HallStatus hallStatus) {
        return Hall
                .builder()
                .hallStatus(hallStatus)
                .build();
    }

    public static Hall create(long id) {
        return Hall
                .builder()
                .id(id)
                .build();
    }
}
