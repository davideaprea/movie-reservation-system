package io.github.davideaprea.security.service;

import io.github.davideaprea.security.component.JWTCreator;
import io.github.davideaprea.security.dto.AuthUserDetails;
import io.github.davideaprea.security.dto.JWTClaims;
import io.github.davideaprea.security.entity.User;
import io.github.davideaprea.security.dto.LoginCreateRequest;
import io.github.davideaprea.security.dto.UserCreateRequest;
import io.github.davideaprea.security.dao.UserDAO;
import io.github.davideaprea.security.enumeration.Role;
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
    private final UserDAO userDao;
    private final JWTCreator jwtCreator;
    private final AuthenticationManager authManager;
    private final PasswordEncoder encoder;

    public User register(UserCreateRequest credentials) {
        return userDao.save(User.builder()
                .email(credentials.email())
                .password(encoder.encode(credentials.password()))
                .role(Role.USER)
                .build());
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
