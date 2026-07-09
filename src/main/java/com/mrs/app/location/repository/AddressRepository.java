package com.mrs.app.location.repository;

import com.mrs.app.location.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
