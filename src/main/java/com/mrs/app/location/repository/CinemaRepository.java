package com.mrs.app.location.repository;

import com.mrs.app.location.entity.Cinema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    Page<Cinema> findAllByAddressIdIn(Pageable pageable, List<Long> addressIds);
}
