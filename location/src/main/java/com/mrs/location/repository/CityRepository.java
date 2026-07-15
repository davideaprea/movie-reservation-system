package com.mrs.location.repository;

import com.mrs.location.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
    Page<City> findAllByRegionId(Pageable pageable, long regionId);
}
