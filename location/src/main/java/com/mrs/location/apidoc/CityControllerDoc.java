package com.mrs.location.apidoc;

import com.mrs.location.dto.CityResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "City endpoints")
public interface CityControllerDoc {
    @Operation(
            summary = "Retrieves all region's cities",
            responses = {
                    @ApiResponse(
                            responseCode = "200"
                    )
            }
    )
    ResponseEntity<Page<CityResponse>> findAllByRegionId(
            @Parameter(required = true, name = "Region id") long regionId,
            Pageable pageable
    );
}
