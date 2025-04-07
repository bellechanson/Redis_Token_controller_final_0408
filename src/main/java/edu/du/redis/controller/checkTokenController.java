package edu.du.redis.controller;

import edu.du.redis.service.RedisService;
import edu.du.redis.service.TokenService;
import edu.du.redis.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        String token = jwtUtil.createToken("token");
        redisTemplate.opsForValue().set(email, token);

        System.out.println(redisService.resolveUserClaims(email).get());
        return token;
    }



    @GetMapping("/read")
    public String readToken() {
        Map<String, Object> token = jwtUtil.extractClaims("token");
        System.out.println(token.get("token"));
        return token.toString();
    }

}
