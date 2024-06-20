package com.uijin.auth.security.utils;

import com.uijin.auth.security.enums.ApiExceptionCode;
import com.uijin.auth.security.exception.BaseApiException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    private final SecretKey secretKey;

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

    public Integer getUserId(String token) {
        return getClaims(token).getPayload().get("userId", Integer.class);
    }

    public Boolean isExpired(String token) {
        return getClaims(token).getPayload().getExpiration().before(new Date());
    }

    public Jws<Claims> getClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
    }

    // 만료된 토큰의 claim은 exception에서 가져올 수 있다.
    public Claims getClaimsFromExpiredToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw BaseApiException.of(ApiExceptionCode.INVALID_TOKEN);
        }

        return null;
    }

    public String createAccessToken(long userId, String username, String role) {
//        long expiredMs = 1000 * 60 * 15;    // 15분
        long expiredMs = 1000;    // 15분
        return "Bearer " + Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    // refreshToken은 유효시간외에는 아무 정보도 담지 않고 있기 때문에, DB에 저장해놓고, 요청 헤더에 담긴 Refresh Token이 해당 사용자의
    // Refresh Token이 맞는지 검증해야한다.
    public String createRefreshToken() {
        long expiredMs = 1000 * 60 * 60 * 24 * 10;  // 10일

        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
