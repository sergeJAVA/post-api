package com.example.post_api.services.security;

import com.example.post_api.constants.RoleType;
import com.example.post_api.model.security.TokenData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class JWTService {
    @Value("${spring.application.name}")
    private String appName;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.life-time}")
    private Long lifeTime;

    public Long getUserIdFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("userId", Long.class));
    }

    public String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public List<String> getRolesFromToken(String token) {
        return getClaimFromToken(token, (Function<Claims, List<String>>) claims -> claims.get("roles", List.class));
    }


    private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
        return claimResolver.apply(getAllClaimsFromToken(token));
    }

    public TokenData parseToken(String token) {
        return TokenData.builder()
                .token(token)
                .username(getUserNameFromToken(token))
                .authorities(getRolesFromToken(token).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()))
                .id(getUserIdFromToken(token))
                .build();
    }

    @SneakyThrows
    private Claims getAllClaimsFromToken(String token){
        try{
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error! Wrong argument passed!");
        }

    }


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
