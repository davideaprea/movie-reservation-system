package com.example.demo.cinema.repository;

import com.example.demo.cinema.projection.SeatProjection;
import com.example.demo.cinema.entity.Seat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SeatDao extends CrudRepository<Seat, Long> {
    @Query("""
        SELECT new com.example.demo.cinema.projection.SeatDetail(
            s.id, s.seatType, s.rowNumber, s.seatNumber, s.hall.id
        )
        FROM Seat s
        WHERE s.id IN :seatIds
    """)
    List<SeatProjection> findAll(List<Long> seatIds);
}
