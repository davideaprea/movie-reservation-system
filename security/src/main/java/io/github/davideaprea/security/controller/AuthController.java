package io.github.davideaprea.security.controller;

import com.mrs.app.security.doc.AuthControllerDocs;
import com.mrs.app.security.dto.UserCreateResponse;
import com.mrs.app.security.entity.User;
import com.mrs.app.security.dto.LoginCreateRequest;
import com.mrs.app.security.dto.UserCreateRequest;
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

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserCreateResponse> register(@RequestBody @Valid UserCreateRequest dto) {
        User newUser = authService.register(dto);

        UserCreateResponse res = new UserCreateResponse(
                newUser.getId(),
                newUser.getEmail()
        );

        return new ResponseEntity<>(res, HttpStatus.CREATED);
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
