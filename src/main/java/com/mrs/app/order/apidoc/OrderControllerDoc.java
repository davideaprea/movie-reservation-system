package com.mrs.app.order.apidoc;

import com.mrs.app.order.dto.HTTPOrderCreateRequest;
import com.mrs.app.order.dto.OrderCreateResponse;
import com.mrs.app.order.dto.OrderGetResponse;
import com.mrs.app.shared.exception.ConflictingResourceError;
import com.mrs.app.shared.exception.DomainRequirementError;
import com.mrs.app.shared.exception.FieldValidationError;
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
        name = "Order endpoints",
        description = "Endpoints for managing booking orders."
)
public interface OrderControllerDoc {
    @Operation(
            summary = "Create a new order",
            description = "Creates a new order for a specific schedule and seats. The order includes booking the seats and generating a intent intent.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Order created successfully.",
                            content = @Content(schema = @Schema(implementation = OrderCreateResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The request payload didn't pass the formal validation.",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = FieldValidationError.class)))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "One or more selected seats are already booked.",
                            content = @Content(schema = @Schema(implementation = ConflictingResourceError.class))
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "The selected schedule is over.",
                            content = @Content(schema = @Schema(implementation = DomainRequirementError.class))
                    )
            }
    )
    ResponseEntity<OrderCreateResponse> create(HTTPOrderCreateRequest request, @Parameter(hidden = true) long loggedUserId);

    @Operation(
            summary = "Find all by logged user id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Orders retrieved successfully.",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderGetResponse.class)))
                    )
            }
    )
    ResponseEntity<List<OrderGetResponse>> findAllByUserId(@Parameter(hidden = true) long loggedUserId);
}
