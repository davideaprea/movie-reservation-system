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
    private Byte rowNumber;

    @Column(nullable = false, updatable = false)
    private Byte seatNumber;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Hall hall;

    public static Seat create(
            SeatType seatType,
            byte rowNumber,
            byte seatNumber
    ) {
        return Seat
                .builder()
                .seatType(seatType)
                .rowNumber(rowNumber)
                .seatNumber(seatNumber)
                .build();
    }
}
