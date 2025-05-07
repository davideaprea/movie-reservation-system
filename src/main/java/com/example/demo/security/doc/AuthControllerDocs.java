package com.example.demo.security.doc;

import com.example.demo.security.dto.LoginDto;
import com.example.demo.security.dto.RegisterDto;
import com.example.demo.security.dto.RegisterResponse;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface AuthControllerDocs {
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterResponse.class)
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
    ResponseEntity<RegisterResponse> register(RegisterDto dto);

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
    ResponseEntity<Void> login(LoginDto dto);
}
