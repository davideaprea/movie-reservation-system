package com.mrs.app.location.dto;

public record AddressResponse(
        long id,
        long cityId,
        String name,
        String number
) {
}
