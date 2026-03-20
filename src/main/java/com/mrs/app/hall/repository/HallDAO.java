package com.mrs.app.hall.repository;

import com.mrs.app.hall.entity.Hall;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface HallDAO extends CrudRepository<Hall, Long> {
    @Query("""
                SELECT h FROM Hall h
                LEFT JOIN FETCH h.seats s
                LEFT JOIN FETCH s.type
                WHERE h.id = :id
            """)
    Optional<Hall> findByIdWithSeats(Long id);
}
