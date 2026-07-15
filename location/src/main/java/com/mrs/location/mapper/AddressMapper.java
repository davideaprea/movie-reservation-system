package com.mrs.location.mapper;

import com.mrs.location.dto.AddressResponse;
import com.mrs.location.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressResponse toResponse(Address address);
}
