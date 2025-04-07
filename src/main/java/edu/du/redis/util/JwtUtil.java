package edu.du.redis.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;


    private Key key;

    public String createToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration); // 예: 1시간 후

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email); // 메서드 매개변수 사용
        claims.put("role", "USER");
        claims.put("userId", 12345);

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(expiration))
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS256) // 순서 바뀜!
                .compact();
    }

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // Claims 추출 (토큰에서 데이터 가져오기)
    public Map<String, Object> extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
