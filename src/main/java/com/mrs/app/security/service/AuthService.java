package com.mrs.app.security.service;

import com.mrs.app.security.component.JWTCreator;
import com.mrs.app.security.dto.*;
import com.mrs.app.security.entity.User;
import com.mrs.app.security.mapper.UserMapper;
import com.mrs.app.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JWTCreator jwtCreator;
    private final AuthenticationManager authManager;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    public UserCreateResponse register(UserCreateRequest request) {
        User user = userRepository.save(User.builder()
                .email(request.email())
                .password(encoder.encode(request.password()))
                .role(request.role())
                .cinemaId(request.cinemaId())
                .build());

        return userMapper.toResponse(user);
    }

    public String login(LoginCreateRequest credentials) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.email(),
                        credentials.password()
                )
        );

        if ((authentication.getPrincipal() instanceof AuthUserDetails userDetails)) {
            return jwtCreator.withSubject(new JWTClaims(
                    userDetails.getUsername(),
                    userDetails.getAuthorities()
                            .stream()
                            .map(SimpleGrantedAuthority::getAuthority)
                            .toList()
            ));
        }

        throw new AuthenticationServiceException("Unexpected principal type: " + authentication.getPrincipal().getClass().getName());
    }
}
