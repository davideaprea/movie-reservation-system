package com.mrs.location.repository;

import com.mrs.location.entity.Cinema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    @Query("""
            SELECT c
            FROM Cinema c
            WHERE c.address.city.id = :cityId
            """)
    Page<Cinema> findAllByCityId(Pageable pageable, long cityId);
}
