package com.example.oauth2study.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@Getter
public class JwtTokenProvider {

    private final long JWT_ACCESS_TOKEN_EXPTIME;
    private final long JWT_REFREASH_TOKEN_EXPTIME;
    private final String JWT_ACCESS_SECRET_KEY;
    private final String JWT_REFRESH_SECRET_KEY;

    private Key accessKey;
    private Key refreshKey;

    public JwtTokenProvider(@Value("${jwt.time.access}") long jwt_access_token_exptime,
                            @Value("${jwt.time.refresh}") long jwt_refreash_token_exptime,
                            @Value("${jwt.secret.access}") String jwt_access_secret_key,
                            @Value("${jwt.secret.refresh}") String jwt_refresh_secret_key) {
        JWT_ACCESS_TOKEN_EXPTIME = jwt_access_token_exptime;
        JWT_REFREASH_TOKEN_EXPTIME = jwt_refreash_token_exptime;
        JWT_ACCESS_SECRET_KEY = jwt_access_secret_key;
        JWT_REFRESH_SECRET_KEY = jwt_refresh_secret_key;
    }

    @PostConstruct
    public void initialize() {
        byte[] accessKeyBytes = Decoders.BASE64.decode(JWT_ACCESS_SECRET_KEY);
        this.accessKey = Keys.hmacShaKeyFor(accessKeyBytes);

        byte[] secretKeyBytes = Decoders.BASE64.decode(JWT_REFRESH_SECRET_KEY);
        this.refreshKey = Keys.hmacShaKeyFor(secretKeyBytes);
    }

    public String createAccessToken(Long userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .setExpiration(new Date(new Date().getTime() + JWT_ACCESS_TOKEN_EXPTIME))
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Long userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .setExpiration(new Date(new Date().getTime() + JWT_REFREASH_TOKEN_EXPTIME))
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserIdFromAccessToken(String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(accessKey)
                .build()
                .parseClaimsJws(accessToken)
                .getBody().getSubject();
    }

    public String getUserIdFromRefreshToken(String refreshToken) {
        return Jwts.parserBuilder()
                .setSigningKey(refreshKey)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody().getSubject();
    }

    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parserBuilder()
                .build()
                .parseClaimsJws(accessToken)
                .getBody().getExpiration();
        return expiration.getTime() - new Date().getTime();
    }
}
