package com.mrs.app.cinema.entity;

import com.mrs.app.cinema.enumeration.HallStatus;
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
    private HallStatus status;

    @OneToMany(
            mappedBy = Seat.Fields.hall,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<Seat> seats;

    public static Hall create() {
        return Hall
                .builder()
                .status(HallStatus.AVAILABLE)
                .build();
    }

    public static Hall createWithId(long id) {
        return Hall
                .builder()
                .id(id)
                .build();
    }
}
