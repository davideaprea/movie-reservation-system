package com.mrs.app.cinema.entity;

import com.mrs.app.cinema.enumeration.SeatType;
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
@Table(name = "seats", uniqueConstraints = {
        @UniqueConstraint(columnNames = "hall_id, row_number, seat_number")
})
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatType type;

    @Column(nullable = false, updatable = false)
    private Integer rowNumber;

    @Column(nullable = false, updatable = false)
    private Integer seatNumber;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Hall hall;

    public static Seat create(
            SeatType type,
            int rowNumber,
            int seatNumber,
            long hallId
    ) {
        return Seat
                .builder()
                .type(type)
                .rowNumber(rowNumber)
                .seatNumber(seatNumber)
                .hall(Hall.createWithId(hallId))
                .build();
    }

    public static Seat createWithId(long id) {
        return Seat
                .builder()
                .id(id)
                .build();
    }
}
