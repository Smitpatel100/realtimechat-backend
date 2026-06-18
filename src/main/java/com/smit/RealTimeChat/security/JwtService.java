package com.smit.RealTimeChat.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final String jwtSecret;
    private final long jwtExpiration;

    public JwtService(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.expiration}") long jwtExpiration
    ) {
        this.jwtSecret = jwtSecret;
        this.jwtExpiration = jwtExpiration;
    }

    public String generateToken(String email) {

        Key signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }
}