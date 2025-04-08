package edu.du.redis.controller;

import edu.du.redis.dto.RedisUser;
import edu.du.redis.dto.Token;
import edu.du.redis.service.RedisService;
import edu.du.redis.service.TokenService;
import edu.du.redis.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/test")
public class checkTokenController {
    private final RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private RedisService redisService;
    private JwtUtil jwtUtil;

    public checkTokenController(JwtUtil jwtUtil, RedisTemplate<Object, Object> redisTemplate) {
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/token")
    public String checkToken() {
        String token = jwtUtil.createToken("token");
        System.out.println(token);
        return token;
    }

    @PostMapping("create")
    public String createToken(@RequestParam String email) {
        String token = jwtUtil.createToken(email); // ✔️ 진짜 이메일로 JWT 생성
        redisService.saveTokenToRedis(email, token); // ✔️ Redis에 저장
        // 클레임 확인 (Optional로 감싸져 있으니 null 체크하거나 예외 처리 추천)
        Optional<Map<String, Object>> claims = redisService.resolveUserClaims(email);
        claims.ifPresent(System.out::println);
        return token;
    }

    @PostMapping("/info")
    public RedisUser getUserInfo(@RequestBody Token tokenDto) {
        String token = tokenDto.getToken();

        Claims claims = jwtUtil.extractClaims(token);

        String email = claims.getSubject(); // subject -> email
        Number idNumber = claims.get("id", Number.class); // 수정됨
        Long id = idNumber != null ? idNumber.longValue() : null;
        String userRole = claims.get("role", String.class); // claim

        System.out.println(email);
        System.out.println(id);
        System.out.println(userRole);

        return new RedisUser(email, id, userRole);
    }




//    @GetMapping("/read")
//    public String readToken(@RequestParam String email) {
//        Optional<String> tokenOptional = redisService.getTokenFromRedis(email);
//        if (tokenOptional.isEmpty()) {
//            return "해당 이메일의 토큰이 Redis에 존재하지 않습니다.";
//        }
//
//        String token = tokenOptional.get();
//        Map<String, Object> claims = jwtUtil.extractClaims(token);
//        return claims.toString();
//    }


}
