package com.sparta.sbug.security.dto;

import io.jsonwebtoken.Jwt;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
public class JwtDto {
    private String grantType; // BEARER
    private String accessToken;
    private String refreshToken;
    private Long refreshTokenExpirationTime;
    @Builder
    public JwtDto(String grantType, String accessToken, String refreshToken, Long refreshTokenExpirationTime) {
        this.grantType = grantType;
        this.accessToken = grantType + accessToken;
        this.refreshToken = grantType + refreshToken;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }
}
