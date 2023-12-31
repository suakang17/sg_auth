package com.suakang.auth.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {
    public static final byte[] secret = "SecretKeySecretKeySecretKeySecretKeySecretKeySecretKey".getBytes();
    private final Key key = Keys.hmacShaKeyFor(secret);

    public String createToken(Map<String, Object> claims, Date expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    public Jwt createJwt(Map<String, Object> claims) {
        String accessToken = createToken(claims, getAccessTokenExpiration());
        String refreshToken = createToken(new HashMap<>(), getRefreshTokenExpiration());
        return Jwt.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Date getAccessTokenExpiration() {
        long expireTimeMils = 1000 * 60 * 60;
        return new Date(System.currentTimeMillis() + expireTimeMils);
    }

    public Date getRefreshTokenExpiration() {
        long expireTimeMils = 1000L * 60 * 60 * 24 * 60;
        return new Date(System.currentTimeMillis() + expireTimeMils);
    }
}
