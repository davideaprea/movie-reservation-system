package io.github.davideaprea.schedule.apidoc;

import io.github.davideaprea.schedule.dto.ScheduleCreateRequest;
import io.github.davideaprea.schedule.dto.ScheduleGetRequestFilters;
import io.github.davideaprea.schedule.dto.ScheduleResponse;
import io.github.davideaprea.shared.exception.ConflictingResourceError;
import io.github.davideaprea.shared.exception.EntityNotFoundError;
import io.github.davideaprea.shared.exception.FieldValidationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(
        name = "Schedule endpoints",
        description = "Endpoints for managing showtime schedules for movies and halls."
)
public interface ScheduleControllerDoc {
    @Operation(
            summary = "Create a new schedule",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Schedule created successfully.",
                            content = @Content(schema = @Schema(implementation = ScheduleResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The request payload didn't pass the formal validation.",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = FieldValidationError.class)))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "The hall is already booked for the requested time slot.",
                            content = @Content(schema = @Schema(implementation = ConflictingResourceError.class))
                    )
            }
    )
    ResponseEntity<ScheduleResponse> create(ScheduleCreateRequest dto);

    @Operation(
            summary = "Get schedules by filters",
            description = "Retrieves a list of schedules filtered by hall, start/end time, or other criteria.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of schedules matching the filters.",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ScheduleResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid filter parameters."
                    )
            }
    )
    ResponseEntity<List<ScheduleResponse>> findAllByFilters(
            @Parameter(
                    required = true
            )
            ScheduleGetRequestFilters filters
    );

    @Operation(
            summary = "Get a schedule by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Schedule found and returned.",
                            content = @Content(schema = @Schema(implementation = ScheduleResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Schedule not found with the given ID.",
                            content = @Content(schema = @Schema(implementation = EntityNotFoundError.class))
                    )
            }
    )
    ResponseEntity<ScheduleResponse> findById(long id);
}
