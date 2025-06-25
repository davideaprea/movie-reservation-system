package com.mrs.app.security.config;

import com.mrs.app.routes.ControllerRoutes;
import com.mrs.app.security.enumeration.Roles;
import com.mrs.app.security.filter.JWTFilter;
import com.mrs.app.security.service.AuthUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthUserDetailsService authUserDetailsService;
    private final JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain configFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(reqMatcher -> reqMatcher
                        .requestMatchers(ControllerRoutes.AUTH + ControllerRoutes.REGISTER).permitAll()
                        .requestMatchers(ControllerRoutes.AUTH + ControllerRoutes.LOGIN).permitAll()
                        .requestMatchers(ControllerRoutes.SWAGGER + "/**").permitAll()
                        .requestMatchers(GET, ControllerRoutes.MOVIES + "/**").permitAll()
                        .requestMatchers(GET, ControllerRoutes.SCHEDULES + "/**").permitAll()
                        .requestMatchers(ControllerRoutes.MOVIES + "/**").hasRole(Roles.ADMIN.toString())
                        .requestMatchers(ControllerRoutes.SCHEDULES + "/**").hasRole(Roles.ADMIN.toString())
                        .requestMatchers(ControllerRoutes.HALLS + "/**").hasRole(Roles.ADMIN.toString())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationProvider configAuthProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(authUserDetailsService);

        return provider;
    }

    @Bean
    public AuthenticationManager configAuthManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}