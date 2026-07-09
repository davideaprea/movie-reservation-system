package com.mrs.app.location.service;

import com.mrs.app.location.dto.AddressResponse;
import com.mrs.app.location.mapper.AddressMapper;
import com.mrs.app.location.repository.AddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public Page<AddressResponse> findAllByCityId(Pageable pageable, long cityId) {
        return addressRepository.findAllByCityId(pageable, cityId)
                .map(addressMapper::toResponse);
    }
}
