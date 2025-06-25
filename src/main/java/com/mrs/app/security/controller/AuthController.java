package com.mrs.app.security.controller;

import com.mrs.app.security.doc.AuthControllerDocs;
import com.mrs.app.security.dto.RegisterResponse;
import com.mrs.app.security.entity.User;
import com.mrs.app.routes.ControllerRoutes;
import com.mrs.app.security.dto.LoginDto;
import com.mrs.app.security.dto.RegisterDto;
import com.mrs.app.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(ControllerRoutes.AUTH)
public class AuthController implements AuthControllerDocs {
    private final AuthService authService;

    @PostMapping(ControllerRoutes.REGISTER)
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterDto dto) {
        User newUser = authService.register(dto);

        RegisterResponse res = new RegisterResponse(
                newUser.getId(),
                newUser.getEmail()
        );

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping(ControllerRoutes.LOGIN)
    public ResponseEntity<Void> login(@RequestBody @Valid LoginDto dto) {
        String token = authService.login(dto);

        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(token);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }
}
