package com.mrs.app.location.dto;

import com.mrs.app.location.enumeration.HallStatus;

import java.util.List;

public record HallGetResponse(
        long id,
        HallStatus status,
        List<SeatGetResponse> seats
) {
}
