package com.mrs.location.apidoc;

import com.mrs.location.dto.RegionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Region endpoints")
public interface RegionControllerDoc {

    @Operation(
            summary = "Retrieves all regions",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = RegionResponse.class
                                            )
                                    )
                            )
                    )
            }
    )
    ResponseEntity<List<RegionResponse>> findAll();
}
