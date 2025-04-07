package edu.du.redis.controller;

import edu.du.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final RedisService RedisService;

    @GetMapping("/check-user")
    public ResponseEntity<?> checkUser(@RequestParam String email) {
        Optional<Map<String, Object>> claims = RedisService.resolveUserClaims(email);

        if (claims.isPresent()) {
            return ResponseEntity.ok(claims.get());
        } else {
            System.out.println(email);
            return ResponseEntity.status(401).body("Invalid or missing token");
        }
    }
}