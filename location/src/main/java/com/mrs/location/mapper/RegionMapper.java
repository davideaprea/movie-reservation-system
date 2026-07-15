package com.mrs.location.mapper;

import com.mrs.location.dto.RegionResponse;
import com.mrs.location.entity.Region;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegionMapper {
    RegionResponse toResponse(Region region);
}
