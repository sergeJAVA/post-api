package com.example.post_api.services.security;

import com.example.post_api.constants.RoleType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JWTService {
    @Value("${spring.application.name}")
    private String appName;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.life-time}")
    private Long lifeTime;


    public String generateDefaultToken() {
        Map<String, Object> claims = new HashMap<>();

        claims.put("username", "");
        claims.put("roles", Stream.of(RoleType.USER.value())
                .collect(Collectors.toSet()));

        return Jwts.builder()
                .claims(claims)
                .subject(appName)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + lifeTime))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}
