package io.github.davideaprea.hall.dto;

import java.util.List;

public record HallResponse(
        long id,
        String name,
        List<SeatResponse> seats
) {
}
