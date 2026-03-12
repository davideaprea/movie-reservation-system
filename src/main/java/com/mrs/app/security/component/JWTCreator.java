package com.mrs.app.security.component;

import com.mrs.app.security.dto.JWTClaims;
import com.mrs.app.security.dto.JWTConfigProps;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@AllArgsConstructor
@Component
public class JWTCreator {
    private final JWTConfigProps jwtConfigProps;

    public String withSubject(JWTClaims claims) {
        Date expDate = new Date(new Date().getTime() + jwtConfigProps.expTime());

        return Jwts
                .builder()
                .subject(claims.subject())
                .claim("roles", claims.roles())
                .expiration(expDate)
                .signWith(jwtConfigProps.key())
                .compact();
    }
}
