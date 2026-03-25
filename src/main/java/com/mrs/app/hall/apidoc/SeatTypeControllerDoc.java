package com.mrs.app.hall.apidoc;

import com.mrs.app.hall.dto.SeatTypeCreateRequest;
import com.mrs.app.hall.dto.SeatTypeResponse;
import com.mrs.app.shared.exception.ConflictingResourceError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(
        name = "Seat type endpoints",
        description = "Endpoints for managing seat types (e.g., STANDARD, VIP) in the cinema."
)
public interface SeatTypeControllerDoc {
    @Operation(
            summary = "Create a new seat type",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Seat type created successfully.",
                            content = @Content(
                                    schema = @Schema(implementation = SeatTypeResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The request payload didn't pass the formal validation."
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Seat type with the same name already exists.",
                            content = @Content(schema = @Schema(implementation = ConflictingResourceError.class))
                    )
            }
    )
    ResponseEntity<SeatTypeResponse> create(SeatTypeCreateRequest request);

    @Operation(
            summary = "Get all seat types",
            description = "Retrieves all available seat types in the cinema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of all seat types.",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = SeatTypeResponse.class)
                                    )
                            )
                    )
            }
    )
    ResponseEntity<List<SeatTypeResponse>> findAll();
}
