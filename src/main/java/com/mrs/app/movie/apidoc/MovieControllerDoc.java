package com.mrs.app.movie.apidoc;

import com.mrs.app.movie.dto.MovieCreateRequest;
import com.mrs.app.movie.dto.MovieResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "Movie endpoints",
        description = "Endpoints for managing movies in the cinema."
)
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
                            description = "The request payload didn't pass the formal validation."
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
                            description = "Movie with the specified ID was not found."
                    )
            }
    )
    ResponseEntity<MovieResponse> findById(long id);
}
