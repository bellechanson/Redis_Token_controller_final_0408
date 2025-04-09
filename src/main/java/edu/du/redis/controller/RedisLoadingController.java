package edu.du.redis.controller;

import edu.du.redis.dto.Reservation;
import edu.du.redis.model.ReservationRequest;
import edu.du.redis.service.RedisLoadingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/reservation")
public class RedisLoadingController {

    private final RedisLoadingService loadingService;

    // 메모리 저장소 역할 (key → ReservationRequest)
    private final Map<String, ReservationRequest> selectStorage = new ConcurrentHashMap<>();

    public RedisLoadingController(RedisLoadingService loadingService) {
        this.loadingService = loadingService;
    }

    // 🔸 예약 요청 → Redis에서 유저 정보 합치기 → key 발급
    @PostMapping("/select")
    public ResponseEntity<String> select(@RequestBody Reservation reservation) {
        System.out.println(reservation);
        Long userId = reservation.getUId();

        Reservation merged = loadingService.mergeUserAndReservation(userId, reservation);

        String key = UUID.randomUUID().toString();
        ReservationRequest request = new ReservationRequest();
        request.setReservation(merged);

        selectStorage.put(key, request);
        return ResponseEntity.ok(key);
    }


    // 🔸 key로 예약 정보 조회 → Reservation 만 꺼내서 반환
    @GetMapping("/select")
    public ResponseEntity<Reservation> getReservation(@RequestParam String key) {
        ReservationRequest request = selectStorage.get(key);
        if (request == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(request.getReservation()); // ✅ DTO만 추출해서 응답
    }
}
