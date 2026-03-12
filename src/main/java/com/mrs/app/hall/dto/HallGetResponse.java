package com.mrs.app.hall.dto;

import com.mrs.app.hall.enumeration.HallStatus;

import java.util.List;

public record HallGetResponse(
        long id,
        HallStatus status,
        List<SeatGetResponse> seats
) {
}
