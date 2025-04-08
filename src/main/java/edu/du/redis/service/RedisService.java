package edu.du.redis.service;

import edu.du.redis.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.du.redis.dto.RedisUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
    // Redis에 Token 올리기 코드 필요!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void saveTokenToRedis(String userId, String token) {
        String redisKey = "JWT_TOKEN:" + userId;
        // 예: 1시간 후 만료되도록 설정
        redisTemplate.opsForValue().set(redisKey, token, 1, TimeUnit.HOURS);
    }
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
// RedisUser 객체 Redis에 저장하는 메서드
public void saveUserToRedis(RedisUser user) throws JsonProcessingException {
    String key = "user:" + user.getId();
    String value = objectMapper.writeValueAsString(user);
    redisTemplate.opsForValue().set(key, value);
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
