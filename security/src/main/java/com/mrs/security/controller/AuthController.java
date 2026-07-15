package com.mrs.security.controller;

import com.mrs.security.doc.AuthControllerDocs;
import com.mrs.security.dto.*;
import com.mrs.security.enumeration.Role;
import com.mrs.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {
    private final AuthService authService;

    @PostMapping("/users")
    public ResponseEntity<UserCreateResponse> registerUser(@RequestBody @Valid HTTPUserCreateRequest dto) {
        return new ResponseEntity<>(authService.register(new UserCreateRequest(
                dto.email(),
                dto.password(),
                Role.USER,
                null
        )), HttpStatus.CREATED);
    }

    @PostMapping("/operators")
    public ResponseEntity<UserCreateResponse> registerAdmin(@RequestBody @Valid HTTPOperatorCreateRequest dto) {
        return new ResponseEntity<>(authService.register(new UserCreateRequest(
                dto.email(),
                dto.password(),
                dto.role(),
                dto.cinemaId()
        )), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginCreateRequest dto) {
        String token = authService.login(dto);

        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(token);
        headers.setAccessControlExposeHeaders(List.of("Authorization"));

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }
}
