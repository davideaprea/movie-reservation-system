package com.example.demo.security.controller;

import com.example.demo.security.doc.AuthControllerDocs;
import com.example.demo.security.dto.RegisterResponse;
import com.example.demo.security.entity.User;
import com.example.demo.security.enumeration.Routes;
import com.example.demo.security.dto.LoginDto;
import com.example.demo.security.dto.RegisterDto;
import com.example.demo.security.service.AuthService;
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
@RequestMapping(Routes.AUTH)
public class AuthController implements AuthControllerDocs {
    private final AuthService authService;

    @PostMapping(Routes.REGISTER)
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterDto dto) {
        User newUser = authService.register(dto);

        RegisterResponse res = new RegisterResponse(
                newUser.getId(),
                newUser.getEmail()
        );

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping(Routes.LOGIN)
    public ResponseEntity<Void> login(@RequestBody @Valid LoginDto dto) {
        String token = authService.login(dto);

        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(token);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }
}
