package com.mrs.location.dto;

import java.util.List;

public record HallResponse(
        long id,
        String name,
        List<SeatResponse> seats,
        long cinemaId
) {
}
