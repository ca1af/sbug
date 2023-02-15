package com.sparta.sbug.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.sbug.security.RedisDao;
import com.sparta.sbug.security.dto.TokenResponse;
import com.sparta.sbug.security.exception.ForbiddenException;
import com.sparta.sbug.user.dto.UserResponseDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final RedisDao redisDao;
    public static final String Bearer = "Bearer ";

    @Value("${jwt.secret.key}")
    protected byte[] byteKey;

    protected Key key;

    @Value("${jwt.live.atk}")
    private Long atkTime;

    @Value("${jwt.live.rtk}")
    private Long rtkTime;

    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(this.byteKey);
    }

    public TokenResponse reissueAtk(UserResponseDto userResponseDto) throws JsonProcessingException {
        String email = userResponseDto.getEmail();
        String rtkInRedis = redisDao.getValues(email);
        if (Objects.isNull(rtkInRedis)) throw new ForbiddenException("인증 정보가 만료되었습니다.");
        String atk = createToken(email, atkTime);
        String rtk = createToken(email, rtkTime);
        redisDao.setValues(email, rtk, Duration.ofMillis(rtkTime));
        return new TokenResponse(atk, rtk);
    }

    public TokenResponse createTokensByLogin(UserResponseDto userResponseDto) throws JsonProcessingException {
        String email = userResponseDto.getEmail();

        String atk = createToken(email, atkTime);
        String rtk = createToken(email, rtkTime);
        redisDao.setValues(email, rtk, Duration.ofMillis(rtkTime));
        return new TokenResponse(atk, rtk);
    }
    public TokenResponse createTokenKakao(String email) throws JsonProcessingException {
        String atk = createToken(email, atkTime);
        String rtk = createToken(email, rtkTime);
        redisDao.setValues(email, rtk, Duration.ofMillis(rtkTime));
        return new TokenResponse(atk, rtk);
    }


    private String createToken(String email, Long tokenLive) {
        //String subjectStr = objectMapper.writeValueAsString(email);
        Claims claims = Jwts.claims()
                .setSubject(email);
        Date date = new Date();
        return Bearer + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + tokenLive))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getSubject(String atk) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(atk).getBody().getSubject();
    }
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public void deleteRtk(String key){
        redisDao.deleteValues(key);
    }
}