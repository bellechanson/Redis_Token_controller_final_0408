package edu.du.redis.service;

import edu.du.redis.dto.RedisUser;
import edu.du.redis.dto.Reservation;
import org.springframework.stereotype.Service;

@Service
public class RedisLoadingService {

    private final RedisService redisService;

    public RedisLoadingService(RedisService redisService) {
        this.redisService = redisService;
    }

    /**
     * Redis에서 사용자 정보를 조회하고 Reservation 객체에 사용자 정보를 세팅하여 반환
     *
     * @param userId
     * @param reservation 공연 예약 정보
     * @return 사용자 정보가 포함된 Reservation 객체
     */
    public Reservation mergeUserAndReservation(Long userId, Reservation reservation) {
        System.out.println(reservation);
        RedisUser redisUser = redisService.getUserFromRedis(reservation.getUId()); // 에러 없음

        if (redisUser == null) {
            throw new IllegalArgumentException("Redis에서 사용자 정보를 찾을 수 없습니다.");
        }

        reservation.setUId(redisUser.getId());
        reservation.setUName(redisUser.getName());

        return reservation;
    }

}
