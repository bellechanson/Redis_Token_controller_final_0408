package edu.du.redis.service;

import edu.du.redis.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.du.redis.dto.RedisUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    public RedisUser getUserFromRedis(Long userId) throws JsonProcessingException {
        String key = "user:" + userId;
        String jsonValue = redisTemplate.opsForValue().get(key);

        if (jsonValue == null) return null;

        return objectMapper.readValue(jsonValue, RedisUser.class);
    }

    // Redis에서 토큰 가져오기
    public Optional<String> getTokenFromRedis(String userId) {
        String redisKey = "JWT_TOKEN:" + userId;
        String token = redisTemplate.opsForValue().get(redisKey);
        return Optional.ofNullable(token);
    }

    // Redis에서 토큰 가져와 검증하고 클레임 추출
    public Optional<Map<String, Object>> resolveUserClaims(String userId) {
        return getTokenFromRedis(userId)
                .filter(jwtUtil::validateToken)
                .map(jwtUtil::extractClaims);
    }
}
