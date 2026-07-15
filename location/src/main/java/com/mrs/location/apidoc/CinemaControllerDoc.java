package com.mrs.location.apidoc;

import com.mrs.location.dto.CinemaCreateRequest;
import com.mrs.location.dto.CinemaResponse;
import com.mrs.shared.exception.ConflictingResourceError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "Cinema endpoints")
public interface CinemaControllerDoc {
    @Operation(
            summary = "Creates a new cinema",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "The cinema has been created successfully.",
                            content = @Content(schema = @Schema(implementation = CinemaResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "The selected address or name are already taken.",
                            content = @Content(schema = @Schema(implementation = ConflictingResourceError.class))
                    )
            }
    )
    ResponseEntity<CinemaResponse> create(@RequestBody(required = true) CinemaCreateRequest request);

    @Operation(
            summary = "Retrieves all city's cinemas",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The cinemas have been retrieved successfully.",
                            content = @Content(schema = @Schema(implementation = CinemaResponse.class))
                    )
            }
    )
    ResponseEntity<Page<CinemaResponse>> findAllByCityId(
            Pageable pageable,
            @Parameter(required = true) long cityId
    );
}
