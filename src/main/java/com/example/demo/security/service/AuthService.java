package com.example.demo.security.service;

import com.example.demo.security.component.JWTManager;
import com.example.demo.security.entity.User;
import com.example.demo.security.dto.LoginDto;
import com.example.demo.security.dto.RegisterDto;
import com.example.demo.security.repository.UserDao;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
@Service
public class AuthService {
    private final UserDao userDao;
    private final JWTManager jwtManager;
    private final AuthenticationManager authManager;
    private final PasswordEncoder encoder;

    public User register(RegisterDto credentials) {
        return userDao.save(User.create(
                credentials.email(),
                encoder.encode(credentials.password())
        ));
    }

    public String login(LoginDto credentials) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.email(),
                        credentials.password()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return jwtManager.generateToken(userDetails.getUsername());
    }
}
