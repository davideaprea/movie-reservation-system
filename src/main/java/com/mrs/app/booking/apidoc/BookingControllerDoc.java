package com.mrs.app.booking.apidoc;

import com.mrs.app.booking.dto.SeatReservationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Booking endpoints")
public interface BookingControllerDoc {
    @Operation(
            summary = "Retrieves all booked seats in the schedule.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Booked seats retrieved successfully.",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SeatReservationResponse.class)))
                    )
            }
    )
    ResponseEntity<List<SeatReservationResponse>> findSeatReservationsByScheduleId(@Parameter(required = true) long scheduleId);
}
