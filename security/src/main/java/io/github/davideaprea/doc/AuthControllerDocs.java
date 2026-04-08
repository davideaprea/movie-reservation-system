package io.github.davideaprea.doc;

import com.mrs.app.security.dto.LoginCreateRequest;
import com.mrs.app.security.dto.UserCreateRequest;
import com.mrs.app.security.dto.UserCreateResponse;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth")
public interface AuthControllerDocs {
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserCreateResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email already taken.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content
            )
    })
    ResponseEntity<UserCreateResponse> register(UserCreateRequest dto);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    content = @Content,
                    headers = @Header(
                            name = "Authorization",
                            description = "JWT for authentication",
                            required = true
                    ),
                    description = "Successful login."
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Wrong credentials.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content
            )
    })
    ResponseEntity<Void> login(LoginCreateRequest dto);
}
