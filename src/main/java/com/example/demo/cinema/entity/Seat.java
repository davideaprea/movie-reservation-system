package com.example.demo.cinema.entity;

import com.example.demo.cinema.enumeration.SeatType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@FieldNameConstants
@ToString
@Getter
@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private SeatType seatType;

    @Column(nullable = false, updatable = false)
    private Integer rowNumber;

    @Column(nullable = false, updatable = false)
    private Integer seatNumber;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Hall hall;

    public static Seat create(
            SeatType seatType,
            int rowNumber,
            int seatNumber,
            long hallId
    ) {
        return Seat
                .builder()
                .seatType(seatType)
                .rowNumber(rowNumber)
                .seatNumber(seatNumber)
                .hall(Hall.create(hallId))
                .build();
    }

    public static Seat create(long id) {
        return Seat
                .builder()
                .id(id)
                .build();
    }
}
