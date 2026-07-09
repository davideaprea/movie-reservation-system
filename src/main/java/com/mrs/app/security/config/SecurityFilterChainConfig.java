package com.mrs.app.security.config;

import com.mrs.app.security.enumeration.Role;
import com.mrs.app.security.filter.UserAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityFilterChainConfig {
    @Bean
    public SecurityFilterChain configFilterChain(HttpSecurity http, UserAuthenticationFilter userAuthenticationFilter) {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(reqMatcher -> reqMatcher
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/users").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/actuator/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/movies/**",
                                "/schedules/**",
                                "/cinemas"
                        ).permitAll()
                        .requestMatchers("/halls/**").hasRole(Role.OPERATOR.toString())
                        .requestMatchers(HttpMethod.POST, "/schedules/**").hasRole(Role.OPERATOR.toString())
                        .requestMatchers(
                                HttpMethod.POST,
                                "/seat-types/**",
                                "/auth/operators",
                                "/movies/**",
                                "/cinemas/**"
                        ).hasRole(Role.ADMIN.toString())
                        .requestMatchers("/seat-types/**").hasAnyRole(Role.ADMIN.toString(), Role.OPERATOR.toString())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
