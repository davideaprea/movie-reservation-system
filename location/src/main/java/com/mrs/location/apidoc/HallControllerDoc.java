package com.mrs.location.apidoc;

import com.mrs.location.dto.HallCreateRequest;
import com.mrs.location.dto.HallGetResponse;
import com.mrs.location.dto.HallResponse;
import com.mrs.shared.exception.ConflictingResourceError;
import com.mrs.shared.exception.FieldValidationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import com.mrs.security.dto.LoggedUser;

import java.util.List;

@Tag(name = "Hall endpoints")
public interface HallControllerDoc {
    @Operation(
            summary = "Create a new hall",
            description = "Creates a new hall with the specified name and seat configuration.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "The hall has been successfully created.",
                            content = @Content(schema = @Schema(implementation = HallResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The request payload didn't pass the formal validation.",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = FieldValidationError.class)))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "A hall with the same name already exists.",
                            content = @Content(schema = @Schema(implementation = ConflictingResourceError.class))
                    )
            }
    )
    ResponseEntity<HallResponse> create(
            @RequestBody(
                    description = "The hall creation request containing name and seat layout.",
                    required = true
            )
            HallCreateRequest createRequest,
            @Parameter(hidden = true)
            LoggedUser loggedUser
    );

    @Operation(
            summary = "Get all cinema halls",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of halls returned successfully.",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = HallGetResponse.class))
                            )
                    )
            }
    )
    ResponseEntity<List<HallGetResponse>> findAllCinemaHalls(
            @Parameter(hidden = true) LoggedUser loggedUser,
            long cinemaId
    );
}
