package edu.du.redis.service;

import edu.du.redis.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.du.redis.dto.RedisUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    // key -> value
    private final StringRedisTemplate redisTemplate1;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    public RedisUser getUserFromRedis(Long userId) {
        String key = "user:" + userId;
        System.out.println(key);
        String jsonValue = redisTemplate1.opsForValue().get(key);

        System.out.println(redisTemplate.opsForValue().get(userId));

        Set<String> keys = redisTemplate.keys(key);
        Set<String> keys1 = redisTemplate1.keys(key);
        System.out.println(keys1);

        System.out.println("📦 Redis Key: " + key);
        System.out.println("📦 Redis Value: " + jsonValue);

        if (jsonValue == null) {
            System.out.println("❌ Redis에 해당 유저 정보가 없습니다. userId = " + userId);
            return null;
        }

        try {
            RedisUser user = objectMapper.readValue(jsonValue, RedisUser.class);
            System.out.println("✅ 역직렬화 성공: " + user);
            return user;
        } catch (JsonProcessingException e) {
            System.out.println("❌ 역직렬화 실패: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
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
