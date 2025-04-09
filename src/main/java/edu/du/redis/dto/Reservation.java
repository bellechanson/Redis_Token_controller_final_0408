package edu.du.redis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class Reservation {
    @JsonProperty
    private Long uId;         // 유저 ID
    @JsonProperty
    private String uName;     // 유저 이름 (백엔드에서 Redis 통해 set)
    @JsonProperty
    private String pTitle;    // 공연 제목
    @JsonProperty
    private String pPlace;    // 공연 장소
    @JsonProperty
    private String pDate;     // 공연 날짜
    @JsonProperty
    private int pPrice;       // 가격
    @JsonProperty
    private int pAllSpot;     // 총 좌석 수
    @JsonProperty
    private Long pId;         // 공연 ID
}
