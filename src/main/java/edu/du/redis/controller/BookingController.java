package edu.du.redis.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.du.redis.dto.RedisUser;
import edu.du.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final RedisService redisService;

    @GetMapping("/check")
    public String checkBooking(@RequestParam Long userId) throws JsonProcessingException {
        RedisUser user = redisService.getUserFromRedis(userId);
        if (user == null) {
            return "레디스에서 사용자 정보를 찾을 수 없습니다.";
        }
        return "예매 가능! 사용자: " + user.getEmail() + " (권한: " + user.getRole() + ")";
    }
}
