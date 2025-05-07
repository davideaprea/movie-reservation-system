package com.example.demo.security.component;

import com.example.demo.security.config.JWTProps;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@AllArgsConstructor
@Component
public class JWTManager {
    private final JWTProps jwtProps;

    public String generateToken(String email) {
        Date expDate = new Date(new Date().getTime() + jwtProps.getExpTime());

        return Jwts
                .builder()
                .subject(email)
                .expiration(expDate)
                .signWith(key())
                .compact();
    }

    public String validateToken(String token) {
        return Jwts
            .parser()
            .verifyWith(key())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    private SecretKey key(){
        return Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(jwtProps.getSecret())
        );
    }
}
