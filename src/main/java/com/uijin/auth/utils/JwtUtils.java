package com.uijin.auth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    private SecretKey secretKey;

    public JwtUtils(@Value("${spring.jwt.secret}")String secretKey) {
        this.secretKey =
                new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {
        return getClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {
        return getClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return getClaims(token).getPayload().getExpiration().before(new Date());
    }

    public Jws<Claims> getClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
    }

    public String createAccessToken(long userId, String username, String role) {
        long expiredMs = 1000 * 60 * 15;    // 15분
        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(long userId, String username) {
        long expiredMs = 1000 * 60 * 60 * 24 * 10;  // 10일

        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
