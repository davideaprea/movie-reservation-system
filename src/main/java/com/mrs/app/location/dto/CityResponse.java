package com.mrs.app.location.dto;

public record CityResponse(
        long id,
        long regionId,
        String name,
        String zipCode
) {
}
