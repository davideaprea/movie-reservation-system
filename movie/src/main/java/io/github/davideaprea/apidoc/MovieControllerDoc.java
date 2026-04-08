package io.github.davideaprea.apidoc;

import com.mrs.app.movie.dto.MovieCreateRequest;
import com.mrs.app.movie.dto.MovieResponse;
import com.mrs.app.shared.exception.EntityNotFoundError;
import com.mrs.app.shared.exception.FieldValidationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Movie endpoints")
public interface MovieControllerDoc {
    @Operation(
            summary = "Create a new movie",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Movie created successfully.",
                            content = @Content(schema = @Schema(implementation = MovieResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The request payload didn't pass the formal validation.",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = FieldValidationError.class)))
                    )
            }
    )
    ResponseEntity<MovieResponse> create(
            @RequestBody(
                    description = "Movie creation request containing title, duration, and other details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = MovieCreateRequest.class))
            )
            MovieCreateRequest dto
    );

    @Operation(
            summary = "Get a movie by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Movie found and returned.",
                            content = @Content(schema = @Schema(implementation = MovieResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Movie with the specified ID was not found.",
                            content = @Content(schema = @Schema(implementation = EntityNotFoundError.class))
                    )
            }
    )
    ResponseEntity<MovieResponse> findById(long id);

    @Operation(
            summary = "Get all movies",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of movies returned successfully.",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = MovieResponse.class))
                            )
                    )
            }
    )
    ResponseEntity<List<MovieResponse>> findAll(String title);
}
