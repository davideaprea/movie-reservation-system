package com.mrs.security.filter;

import com.mrs.security.component.JWTValidator;
import com.mrs.security.dto.AuthUserDetails;
import com.mrs.security.service.AuthUserDetailsService;
import com.mrs.shared.model.LoggedUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@AllArgsConstructor
@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {
    private final JWTValidator jwtValidator;
    private final AuthUserDetailsService authUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional
                .ofNullable(request.getHeader("Authorization"))
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> {
                    String headerValue = header.substring(7);

                    try {
                        return jwtValidator.extractSubject(headerValue);
                    } catch (Throwable e) {
                        return null;
                    }
                })
                .ifPresent(email -> {
                    AuthUserDetails userDetails = authUserDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(new LoggedUser(
                            userDetails.getId(),
                            userDetails.getRole(),
                            userDetails.getCinemaId()
                    ), null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                });

        filterChain.doFilter(request, response);
    }
}
