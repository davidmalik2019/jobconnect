
package com.jobconnect.jobconnect;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long EXPIRATION_TIME =
            1000 * 60 * 60; // 1 hour


    // =========================================
    // GET SIGNING KEY
    // =========================================

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );
    }


    // =========================================
    // GENERATE JWT
    // =========================================

    public String generateToken(
            String username,
            String role) {

        return Jwts.builder()

                .subject(username)

                .claim("role", role)

                .issuedAt(new Date())

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                + EXPIRATION_TIME
                        )
                )

                .signWith(getSigningKey())

                .compact();
    }


    // =========================================
    // EXTRACT USERNAME
    // =========================================

    public String extractUsername(String token) {

        Claims claims =
                Jwts.parser()

                        .verifyWith(getSigningKey())

                        .build()

                        .parseSignedClaims(token)

                        .getPayload();

        return claims.getSubject();
    }


    // =========================================
    // EXTRACT ROLE
    // =========================================

    public String extractRole(String token) {

        Claims claims =
                Jwts.parser()

                        .verifyWith(getSigningKey())

                        .build()

                        .parseSignedClaims(token)

                        .getPayload();

        return claims.get(
                "role",
                String.class
        );
    }


    // =========================================
    // VALIDATE TOKEN
    // =========================================

    public boolean isTokenValid(
            String token,
            String username) {

        try {

            Claims claims =
                    Jwts.parser()

                            .verifyWith(getSigningKey())

                            .build()

                            .parseSignedClaims(token)

                            .getPayload();

            String extractedUsername =
                    claims.getSubject();

            Date expiration =
                    claims.getExpiration();

            return extractedUsername != null
                    && extractedUsername.equals(username)
                    && expiration != null
                    && expiration.after(new Date());

        } catch (Exception e) {

            return false;
        }
    }
}
