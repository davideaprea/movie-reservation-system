package com.mrs.app.location.repository;

import com.mrs.app.location.entity.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}
