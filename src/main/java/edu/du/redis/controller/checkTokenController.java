package edu.du.redis.controller;

import edu.du.redis.service.RedisService;
import edu.du.redis.service.TokenService;
import edu.du.redis.util.JwtUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
public class checkTokenController {
    private JwtUtil jwtUtil;

    public checkTokenController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/token")
    public String checkToken() {
        String token = jwtUtil.createToken("token");
        System.out.println(token);
        return token;
    }

    @GetMapping("/read")
    public String readToken() {
        Map<String, Object> token = jwtUtil.extractClaims("token");
        System.out.println(token.get("token"));
        return token.toString();
    }

}
