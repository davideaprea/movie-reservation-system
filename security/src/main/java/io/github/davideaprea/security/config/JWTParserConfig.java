package io.github.davideaprea.security.config;

import com.mrs.app.security.dto.JWTConfigProps;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTParserConfig {
    @Bean
    public JwtParser configJwtParser(JWTConfigProps jwtConfigProps) {
        return Jwts
                .parser()
                .verifyWith(jwtConfigProps.key())
                .build();
    }
}
