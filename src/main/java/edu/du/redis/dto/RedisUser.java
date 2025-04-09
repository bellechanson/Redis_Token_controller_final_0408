package edu.du.redis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RedisUser {
    private Long id;         // 사용자 ID
    private String name;     // 사용자 이름
    private String email;    // 이메일
    private String role;     // 권한 등
}