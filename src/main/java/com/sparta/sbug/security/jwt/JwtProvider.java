package com.sparta.sbug.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.sbug.security.RedisDao;
import com.sparta.sbug.security.dto.TokenResponse;
import com.sparta.sbug.security.exception.ForbiddenException;
import com.sparta.sbug.user.dto.UserResponseDto;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import com.sparta.sbug.security.jwt.Subject;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final RedisDao redisDao;
    private final ObjectMapper objectMapper;

    @Value("${jwt.secret.key}")
    private String key;

    @Value("${jwt.live.atk}")
    private Long atkTime;

    @Value("${jwt.live.rtk}")
    private Long rtkTime;

    @PostConstruct
    protected void init() {
        key = Base64.getEncoder().encodeToString(key.getBytes());
    }

    public TokenResponse reissueAtk(UserResponseDto userResponseDto) throws JsonProcessingException {
        String rtkInRedis = redisDao.getValues(userResponseDto.getEmail());
        if (Objects.isNull(rtkInRedis)) throw new ForbiddenException("인증 정보가 만료되었습니다.");
        Subject atkSubject = Subject.atk(
                userResponseDto.getUserId(),
                userResponseDto.getEmail(),
                userResponseDto.getNickname());
        String atk = createToken(atkSubject, atkTime);
        return new TokenResponse(atk, null);
    }

    public TokenResponse createTokensByLogin(UserResponseDto userResponseDto) throws JsonProcessingException {
        Subject atkSubject = com.sparta.sbug.security.jwt.Subject.atk(
                userResponseDto.getUserId(),
                userResponseDto.getEmail(),
                userResponseDto.getNickname());
        Subject rtkSubject = Subject.rtk(
                userResponseDto.getUserId(),
                userResponseDto.getEmail(),
                userResponseDto.getNickname());
        String atk = createToken(atkSubject, atkTime);
        String rtk = createToken(rtkSubject, rtkTime);
        redisDao.setValues(userResponseDto.getEmail(), rtk, Duration.ofMillis(rtkTime));
        return new TokenResponse(atk, rtk);
    }

    private String createToken(Subject subject, Long tokenLive) throws JsonProcessingException {
        String subjectStr = objectMapper.writeValueAsString(subject);
        Claims claims = Jwts.claims()
                .setSubject(subjectStr);
        Date date = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + tokenLive))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public Subject getSubject(String atk) throws JsonProcessingException {
        String subjectStr = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(atk).getBody().getSubject();
        return objectMapper.readValue(subjectStr, Subject.class);
    }
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}