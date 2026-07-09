package com.mrs.app.location.mapper;

import com.mrs.app.location.dto.AddressResponse;
import com.mrs.app.location.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressResponse toResponse(Address address);
}
