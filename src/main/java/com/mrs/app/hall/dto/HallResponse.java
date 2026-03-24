package com.mrs.app.hall.dto;

import java.util.List;

public record HallResponse(
        long id,
        String name,
        List<SeatResponse> seats
) {
}
