package edu.du.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import edu.du.redis.util.JwtUtil;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, String> redisTemplate;


    /**
     * JWT 토큰을 Redis에 저장합니다.
     * @param email 사용자 이메일 (Key)
     * @param token JWT 토큰 (Value)
     */
    public void saveTokenToRedis(String email, String token) {
        // Redis에 토큰 저장 (예: 1시간 유효)
        redisTemplate.opsForValue().set("jwt:" + email, token, Duration.ofHours(1));
    }

    /**
     * 저장된 토큰을 가져옵니다.
     * @param email 사용자 이메일
     * @return 저장된 토큰
     */
    public String getTokenFromRedis(String email) {
        System.out.println(redisTemplate.opsForValue().get("jwt" + email));
        return redisTemplate.opsForValue().get("jwt:" + email);
    }

    /**
     * 로그아웃 시 토큰 삭제
     */
    public void deleteToken(String email) {
        redisTemplate.delete("jwt:" + email);
    }
}